package ru.gb.client.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gb.client.core.ClientPipelineCheckoutService;
import ru.gb.client.service.Callback;
import ru.gb.common.Command;
import ru.gb.common.CommandType;

import java.io.*;
import java.util.Arrays;

public class FilesInboundClientHandler extends ChannelInboundHandlerAdapter {

    private final String fileName;
    private final String userDirectory;
    private final Long fileSize;
    private final Callback setButtonsAbleAndUpdateFilesLIstCallback;

    private static final Logger LOGGER = LogManager.getLogger(FilesInboundClientHandler.class);

    public FilesInboundClientHandler(String fileName, String userDirectory, Long fileSize, Callback setButtonsAbleAndUpdateFilesLIstCallback) {
        this.fileName = fileName;
        this.userDirectory = userDirectory;
        this.fileSize = fileSize;
        this.setButtonsAbleAndUpdateFilesLIstCallback = setButtonsAbleAndUpdateFilesLIstCallback;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object chunkedFile) throws Exception {
        ByteBuf byteBuf = (ByteBuf) chunkedFile;

        String absoluteFileNameForClient = userDirectory + "\\" + fileName;
        File newfile = new File(absoluteFileNameForClient);
        newfile.createNewFile();

        LOGGER.info("Создан файл и запущен процесс приема файла на клиенте по пути " + absoluteFileNameForClient);

        wrightNewFileContent(absoluteFileNameForClient, byteBuf);

        createAnswerAboutSuccessDownload(newfile, ctx);

        setButtonsAbleAndUpdateFilesLIstCallback.callback();
    }

    private void wrightNewFileContent(String absoluteFileNameForClient, ByteBuf byteBuf) throws IOException {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(absoluteFileNameForClient, true))) {
            while (byteBuf.isReadable()) {
                out.write(byteBuf.readByte());
            }
            byteBuf.release();
        }
    }

    private void createAnswerAboutSuccessDownload(File file, ChannelHandlerContext ctx) {
        if (file.length() == fileSize) {
            LOGGER.info("Файл вычитан");

            ClientPipelineCheckoutService.createBasePipelineAfterDownloadForInOutCommandTraffic(ctx, setButtonsAbleAndUpdateFilesLIstCallback);

            String[] args = {fileName};
            ctx.writeAndFlush(new Command(CommandType.FINISHED_DOWNLOAD.toString(), args));

            LOGGER.info("На сервер с клиента отправлена команда FINISHED_DOWNLOAD с аргументами " + Arrays.asList(args));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.throwing(Level.ERROR, cause);
    }

}
