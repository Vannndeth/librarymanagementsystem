package co.istad.service;

import co.istad.connection.ConnectionDb;
import co.istad.dao.MainFeatureDao;
import co.istad.model.User;
import co.istad.storage.Storage;
import co.istad.util.Helper;
import co.istad.util.Singleton;
import co.istad.view.HelperView;

import java.sql.Connection;

public class LoginService {
    private final Connection connection;
    private final Storage storage;

    public LoginService() {
        connection = ConnectionDb.getConnection();
        storage = Singleton.getStorage();
    }

    public User login(User user) {
        User res = MainFeatureDao.login(user, connection);
        if (res.getId() == null) {
            System.out.println("Invalid username or password. Please try again.");
            return null; // No valid user found
        }
        storage.setId(res.getId());
        storage.setUsername(res.getUsername());
        storage.setRole(res.getRole());
        // Successful login
        return res;
    }
}