package ru.gb.server.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gb.common.Command;
import ru.gb.common.CommandType;
import ru.gb.server.core.ServerPipelineCheckoutService;

import java.io.*;

public class FilesInboundHandler extends ChannelInboundHandlerAdapter {

    private final String fileName;
    private final String userDirectory;
    private final String login;
    private final Long fileSize;

    public FilesInboundHandler(String fileName, String userDirectory, String login, Long fileSize) {
        this.fileName = fileName;
        this.userDirectory = userDirectory;
        this.login = login;
        this.fileSize = fileSize;
    }

    private static final Logger LOGGER = LogManager.getLogger(FilesInboundHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object chunkedFile) throws Exception {
        ByteBuf byteBuf = (ByteBuf) chunkedFile;

        String absoluteFileNameForCloud = userDirectory + "\\" + fileName;
        File newfile = new File(absoluteFileNameForCloud);
        newfile.createNewFile();
        LOGGER.info("Создан файл и запущен прием фала а сервере по пути " + absoluteFileNameForCloud);

        wrightNewFileContent(absoluteFileNameForCloud, byteBuf);

        createAnsweraboutSuccessUpload(newfile, ctx);

    }

    private void wrightNewFileContent(String absoluteFileNameForCloud, ByteBuf byteBuf) throws IOException {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(absoluteFileNameForCloud, true))) {
            while (byteBuf.isReadable()) {
                out.write(byteBuf.readByte());
            }
            byteBuf.release();
        }
    }

    private void createAnsweraboutSuccessUpload(File file, ChannelHandlerContext ctx) {
        if (file.length() == fileSize) {
            LOGGER.info("Файл вычитан");
            ServerPipelineCheckoutService.createBasePipelineAfterUploadForInOutCommandTraffic(ctx);

            String[] args = {fileName, login};
            ctx.writeAndFlush(new Command(CommandType.UPLOAD_FINISHED.toString(), args));
            LOGGER.info("На клиент с сервера отправлена команда UPLOAD_FINISHED с аргументами " + args);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.throwing(Level.ERROR, cause);
    }

}
