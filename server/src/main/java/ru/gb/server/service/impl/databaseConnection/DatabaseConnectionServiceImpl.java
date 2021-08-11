package ru.gb.server.service.impl.databaseConnection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gb.server.service.DatabaseConnectionService;
import ru.gb.server.service.impl.ServerPropertiesReciever;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnectionServiceImpl implements DatabaseConnectionService {

    private final Connection connection;
    private final Statement stmt;

    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnectionServiceImpl.class);

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Statement getStmt() {
        return stmt;
    }

    public DatabaseConnectionServiceImpl() {
        try {
            Class.forName(ServerPropertiesReciever.getDbDriver());
            this.connection = DriverManager.getConnection(ServerPropertiesReciever.getDbURL());
            this.stmt = connection.createStatement();
            LOGGER.info("Сервер подключен к базе данных");
        } catch (ClassNotFoundException | SQLException throwables) {
            LOGGER.throwing(Level.ERROR, throwables);
            throw new RuntimeException("Не удается подключиться к базе данных");
        }
    }

    @Override
    public void closeConnection() {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOGGER.throwing(Level.ERROR, e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.throwing(Level.ERROR, e);
            }
        }
    }

}
