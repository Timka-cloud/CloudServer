package ru.gb.server.factory;

import ru.gb.server.core.NettyServerService;
import ru.gb.server.core.ServerService;
import ru.gb.server.service.AuthenticationService;
import ru.gb.server.service.CommandDictionaryService;
import ru.gb.server.service.CommandService;
import ru.gb.server.service.DatabaseConnectionService;
import ru.gb.server.service.impl.CommandDictionaryServiceImpl;
import ru.gb.server.service.impl.ListOfClientFilesInCloudCreatingService;
import ru.gb.server.service.impl.command.AuthenticationCommand;
import ru.gb.server.service.impl.command.ListCreatingCommand;
import ru.gb.server.service.impl.databaseConnection.AuthenticationServiceImpl;
import ru.gb.server.service.impl.databaseConnection.DatabaseConnectionServiceImpl;

import java.util.Arrays;
import java.util.List;

public class Factory {

    public static ServerService getServerService() {
        return new NettyServerService();
    }

    public static CommandDictionaryService getCommandDictionaryService() {
        return new CommandDictionaryServiceImpl();
    }

    public static List<CommandService> getCommandServices() {
        return Arrays.asList(new AuthenticationCommand(), new ListCreatingCommand());
    }

    public static DatabaseConnectionService getDatabaseConnectionService() {
        return new DatabaseConnectionServiceImpl();
    }

    public static AuthenticationService getAuthenticationService() {
        return new AuthenticationServiceImpl();
    }


    public static ListOfClientFilesInCloudCreatingService getListOfFilesService() {
        return new ListOfClientFilesInCloudCreatingService();
    }

}
