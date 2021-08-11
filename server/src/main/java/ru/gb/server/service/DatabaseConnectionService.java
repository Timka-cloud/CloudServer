package ru.gb.server.service;

import java.sql.Connection;
import java.sql.Statement;

public interface DatabaseConnectionService {

    Statement getStmt();

    void closeConnection();

    Connection getConnection();

}
