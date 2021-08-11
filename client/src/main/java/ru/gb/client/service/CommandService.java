package ru.gb.client.service;

import ru.gb.common.Command;

public interface CommandService {

    void processCommand(Command command);

    String getCommand();

}
