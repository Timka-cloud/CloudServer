package ru.gb.server.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gb.common.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ListOfClientFilesInCloudCreatingService {

    private final Path currentPath = Paths.get(ServerPropertiesReciever.getCloudDirectory());
    private static final Logger LOGGER = LogManager.getLogger(ListOfClientFilesInCloudCreatingService.class);


    public List<FileInfo> createServerFilesList(String login) {
        try {
            Path userDirectory = currentPath.resolve(login);
            List<FileInfo> list = Files.list(userDirectory).map(FileInfo::new).collect(Collectors.toList());
            return list;
        } catch (IOException e) {
            LOGGER.throwing(Level.ERROR, e);
            return null;
        }
    }

}
