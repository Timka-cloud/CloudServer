package ru.gb.client.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import ru.gb.client.core.handler.ClientInboundCommandHandler;
import ru.gb.client.core.handler.FilesInboundClientHandler;
import ru.gb.client.service.Callback;

public class ClientPipelineCheckoutService {

    public static void createPipelineForFilesSending(ChannelHandlerContext ctx) {
        ctx.pipeline().addLast(new ChunkedWriteHandler());
        ctx.pipeline().remove(ObjectEncoder.class);
    }

    public static void createBasePipelineAfterUploadForInOutCommandTraffic(ChannelHandlerContext ctx) {
        ctx.pipeline().remove(ChunkedWriteHandler.class);
        ctx.pipeline().remove(ObjectDecoder.class);
        ctx.pipeline().addFirst(new ObjectEncoder());
        ctx.pipeline().addFirst(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
    }

    public static void createPipelineForInboundFilesRecieving(ChannelHandlerContext ctx, String filename,
                                                              String userDirectory, Long filesize,
                                                              Callback setButtonsAbleAndUpdateFilesLIstCallback) {
        ctx.pipeline().remove(ClientInboundCommandHandler.class);
        ctx.pipeline().remove(ObjectDecoder.class);
        ctx.pipeline().addLast(new ChunkedWriteHandler());
        ctx.pipeline().addLast(new FilesInboundClientHandler(filename, userDirectory, filesize, setButtonsAbleAndUpdateFilesLIstCallback));
    }

    public static void createBasePipelineAfterDownloadForInOutCommandTraffic(ChannelHandlerContext ctx, Callback callback) {
        ctx.pipeline().remove(ChunkedWriteHandler.class);
        ctx.pipeline().addFirst(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        ctx.pipeline().addLast(new ClientInboundCommandHandler(callback));
        ctx.pipeline().remove(FilesInboundClientHandler.class);
    }

}
