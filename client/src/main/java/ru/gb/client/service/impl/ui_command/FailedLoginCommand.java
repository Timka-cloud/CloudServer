package ru.gb.client.service.impl.ui_command;

import ru.gb.client.MainClientApp;
import ru.gb.client.controller.Controller;
import ru.gb.client.service.CommandService;
import ru.gb.common.Command;
import ru.gb.common.CommandType;

public class FailedLoginCommand implements CommandService {

    @Override
    public void processCommand(Command command) {
        Controller currentController = (Controller) MainClientApp.getActiveController();
        currentController.createAlertOnGUI("Такой логин уже существует" +
                " с другим паролем. Введите другую пару логин/пароль для регистрации");
    }

    @Override
    public String getCommand() {
        return CommandType.REGISTRATION_FAILED.toString();
    }
}
