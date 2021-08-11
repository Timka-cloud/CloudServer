package ru.gb.server;

import ru.gb.server.factory.Factory;

public class MainServerApp {

    public static void main(String[] args) {

        Factory.getServerService().startServer();

    }
}

