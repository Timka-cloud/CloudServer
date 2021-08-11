package ru.gb.server.service.impl.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gb.common.Command;
import ru.gb.common.CommandType;
import ru.gb.common.FileInfo;
import ru.gb.server.factory.Factory;
import ru.gb.server.service.CommandService;
import ru.gb.server.service.impl.ListOfClientFilesInCloudCreatingService;

import java.util.List;

public class ListCreatingCommand implements CommandService {

    private static final Logger LOGGER = LogManager.getLogger(ListCreatingCommand.class);

    @Override
    public List<FileInfo> processCommand(Command command) {
        final int requirementCountCommandArgs = 1;
        if (command.getArgs().length != requirementCountCommandArgs) {
            LOGGER.error("Command " + getCommand() + "is not correct");
            throw new IllegalArgumentException("Command " + getCommand() + "is not correct");
        }
        return process((String) command.getArgs()[0]);
    }

    private List<FileInfo> process(String login) {
        ListOfClientFilesInCloudCreatingService listOfClientFilesInCloudCreatingService = Factory.getListOfFilesService();
        return listOfClientFilesInCloudCreatingService.createServerFilesList(login);
    }

    @Override
    public String getCommand() {
        return CommandType.FILESLIST.toString();
    }
}
