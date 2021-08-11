package ru.gb.client.service.impl.ui_command;

import ru.gb.client.MainClientApp;
import ru.gb.client.controller.Controller;
import ru.gb.client.service.CommandService;
import ru.gb.common.Command;
import ru.gb.common.CommandType;
import ru.gb.common.FileInfo;

import java.util.List;

public class CloudFilesListCommand implements CommandService {

    @Override
    public void processCommand(Command command) {
        Controller currentController = (Controller) MainClientApp.getActiveController();

        currentController.createServerListFilesOnGUI((String) command.getArgs()[0], (List<FileInfo>) command.getArgs()[1]);
    }

    @Override
    public String getCommand() {
        return CommandType.CLOUD_FILESLIST.toString();
    }
}
