package ru.gb.client.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedFile;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gb.client.core.handler.ClientInboundCommandHandler;
import ru.gb.client.service.Callback;
import ru.gb.client.service.impl.ClientPropertiesReciever;
import ru.gb.common.Command;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class NettyNetworkService implements NetworkService {

    private static SocketChannel channel;

    private static final String SERVER_HOST = ClientPropertiesReciever.getHost();
    private static final int SERVER_PORT = ClientPropertiesReciever.getPort();

    private static final Logger LOGGER = LogManager.getLogger(NettyNetworkService.class);

    private NettyNetworkService() {
    }

    public static NettyNetworkService initializeNetwork(Callback setButtonsAbleAndUpdateFilesLIstCallback) {
        NettyNetworkService network = new NettyNetworkService();
        initializeNetworkService(setButtonsAbleAndUpdateFilesLIstCallback);
        return network;
    }

    private static void initializeNetworkService(Callback setButtonsAbleAndUpdateFilesLIstCallback) {
        Thread t = new Thread(() -> {
            EventLoopGroup workGroup = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) {
                                channel = socketChannel;
                                socketChannel.pipeline()
                                        .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                        .addLast(new ObjectEncoder())
                                        .addLast(new ClientInboundCommandHandler(setButtonsAbleAndUpdateFilesLIstCallback));
                            }
                        });
                ChannelFuture future = bootstrap.connect(SERVER_HOST, SERVER_PORT).sync();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                LOGGER.throwing(Level.ERROR, e);
            } finally {
                workGroup.shutdownGracefully();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void sendCommand(Command command) {
        channel.writeAndFlush(command);
        LOGGER.info("С клиента на сервер отправлена команда " + command.getCommandName() + " с аргументами " + Arrays.asList(command.getArgs()));
    }

    @Override
    public void sendFile(String pathToFile) {
        try {
            ChannelFuture future = channel.writeAndFlush(new ChunkedFile(new File(pathToFile)));
            LOGGER.info("Началась передача файла на сервер по пути " + pathToFile);
            future.addListener((ChannelFutureListener) channelFuture -> LOGGER.info("Файл передан"));
        } catch (IOException e) {
            LOGGER.throwing(Level.ERROR, e);
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (isConnected()) {
                channel.close().sync();
            }
        } catch (InterruptedException e) {
            LOGGER.throwing(Level.ERROR, e);
        }
    }

    @Override
    public boolean isConnected() {
        return channel != null && !channel.isShutdown();
    }

}

