package ru.gb.client.core;

import ru.gb.common.Command;

public interface NetworkService {

    void sendCommand (Command command);

    void closeConnection();

    boolean isConnected();

    void sendFile (String pathToFile);

}
