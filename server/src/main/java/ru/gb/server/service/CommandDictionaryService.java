package ru.gb.server.service;

import ru.gb.common.Command;

public interface CommandDictionaryService {

    Object processCommand(Command command);
}
