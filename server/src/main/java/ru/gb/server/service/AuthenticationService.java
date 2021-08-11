package ru.gb.server.service;

public interface AuthenticationService {

    boolean isClientRegistered(String login, String password);

    boolean isLoginBusy(String login);

    boolean registerClient(String login, String password);

}
