package ru.gb.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gb.server.core.handler.CommandInboundHandler;
import ru.gb.server.factory.Factory;
import ru.gb.server.service.DatabaseConnectionService;
import ru.gb.server.service.impl.ServerPropertiesReciever;

public class NettyServerService implements ServerService {

    private static final int SERVER_PORT = ServerPropertiesReciever.getPort();
    private static DatabaseConnectionService databaseConnectionService;
    private static final Logger LOGGER = LogManager.getLogger(NettyServerService.class);

    public static DatabaseConnectionService getDatabaseConnectionService() {
        return databaseConnectionService;
    }

    @Override
    public void startServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel) {
                            channel.pipeline()
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new CommandInboundHandler());

                        }
                    });

            ChannelFuture future = bootstrap.bind(SERVER_PORT).sync();
            LOGGER.info("Сервер запущен");
            databaseConnectionService = Factory.getDatabaseConnectionService();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.info("Сервер упал");
            LOGGER.throwing(Level.ERROR, e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            databaseConnectionService.closeConnection();
        }
    }

}
