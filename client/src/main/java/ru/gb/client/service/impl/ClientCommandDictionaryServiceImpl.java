package ru.gb.client.service.impl;

import ru.gb.client.factory.Factory;
import ru.gb.client.service.CommandDictionaryService;
import ru.gb.client.service.CommandService;
import ru.gb.common.Command;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientCommandDictionaryServiceImpl implements CommandDictionaryService {

    private final Map<String, CommandService> commandDictionary;

    public ClientCommandDictionaryServiceImpl() {
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
    public void processCommand(Command command) {
        if (commandDictionary.containsKey(command.getCommandName())) {
            commandDictionary.get(command.getCommandName()).processCommand(command);
        }
    }

}
