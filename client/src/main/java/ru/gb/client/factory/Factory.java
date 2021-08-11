package ru.gb.client.factory;

import ru.gb.client.core.NettyNetworkService;
import ru.gb.client.core.NetworkService;
import ru.gb.client.service.Callback;
import ru.gb.client.service.CommandDictionaryService;
import ru.gb.client.service.CommandService;
import ru.gb.client.service.impl.ClientCommandDictionaryServiceImpl;
import ru.gb.client.service.impl.ui_command.*;

import java.util.Arrays;
import java.util.List;

public class Factory {

    public static NetworkService initializeNetworkService(Callback setButtonsAbleCallback) {
        return NettyNetworkService.initializeNetwork(setButtonsAbleCallback);
    }

    public static CommandDictionaryService getCommandDictionary() {
        return new ClientCommandDictionaryServiceImpl();
    }

    public static List<CommandService> getCommandServices() {
        return Arrays.asList(new AcceptedLoginCommand(), new FailedLoginCommand(),
                new CloudFilesListCommand(), new UploadFileCommand());
    }

}
