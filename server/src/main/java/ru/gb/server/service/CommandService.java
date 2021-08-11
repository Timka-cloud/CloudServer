package ru.gb.server.service;

import ru.gb.common.Command;

public interface CommandService {

    Object processCommand(Command command);

    String getCommand();

}
