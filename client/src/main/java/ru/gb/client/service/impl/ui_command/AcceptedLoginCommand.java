package ru.gb.client.service.impl.ui_command;

import ru.gb.client.MainClientApp;
import ru.gb.client.controller.Controller;
import ru.gb.client.service.CommandService;
import ru.gb.common.Command;
import ru.gb.common.CommandType;

public class AcceptedLoginCommand implements CommandService {

    @Override
    public void processCommand(Command command) {
        Controller currentController = (Controller) MainClientApp.getActiveController();

        currentController.setLogin((String) command.getArgs()[0]);
        currentController.changeLoginPanelToWorkPanel();

        String[] args = {currentController.getLogin()};
        currentController.sendCommand(new Command(CommandType.FILESLIST.toString(), args));
    }

    @Override
    public String getCommand() {
        return CommandType.LOGIN_OK.toString();
    }
}
