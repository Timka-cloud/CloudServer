package ru.gb.server.service.impl;

import ru.gb.common.Command;
import ru.gb.server.factory.Factory;
import ru.gb.server.service.CommandDictionaryService;
import ru.gb.server.service.CommandService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDictionaryServiceImpl implements CommandDictionaryService {

    private final Map<String, CommandService> commandDictionary;

    public CommandDictionaryServiceImpl() {
        this.commandDictionary = Collections.unmodifiableMap(getCommandDictionary());
    }

    private Map<String, CommandService> getCommandDictionary() {
        List<CommandService> commandServices = Factory.getCommandServices();
        Map<String, CommandService> commandDictionary = new HashMap<>();
        for (CommandService commandService : commandServices) {
            commandDictionary.put(commandService.getCommand(), commandService);
        }
        return commandDictionary;
    }

    @Override
    public Object processCommand(Command command) {

        if (commandDictionary.containsKey(command.getCommandName())) {
            return commandDictionary.get(command.getCommandName()).processCommand(command);
        }
        return null;
    }
}
