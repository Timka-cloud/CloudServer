package ru.gb.client.service.impl.ui_command;

import ru.gb.client.MainClientApp;
import ru.gb.client.controller.Controller;
import ru.gb.client.service.CommandService;
import ru.gb.common.Command;
import ru.gb.common.CommandType;

public class UploadFileCommand implements CommandService {

    @Override
    public void processCommand(Command command) {
        Controller currentController = (Controller) MainClientApp.getActiveController();
        currentController.sendFile((String) command.getArgs()[1]);
    }

    @Override
    public String getCommand() {
        return CommandType.READY_TO_UPLOAD.toString();
    }
}
