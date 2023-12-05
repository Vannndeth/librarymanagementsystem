package co.istad.service;

import co.istad.connection.ConnectionDb;
import co.istad.dao.MainFeatureDao;
import co.istad.model.User;
import co.istad.view.HelperView;

import java.sql.Connection;

public class SignupService {
    private final Connection connection;
    public SignupService() {
        connection = ConnectionDb.getConnection();
    }

    public User signup(User user) {
        return MainFeatureDao.create(user, connection);
    }
}
