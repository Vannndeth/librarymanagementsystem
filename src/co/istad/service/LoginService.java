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
    private Connection connection;
    private Storage storage;
    public LoginService(){
        connection = ConnectionDb.getConnection();
        storage = Singleton.getStorage();
    }
    public User login( User user ){
        System.out.println(MainFeatureDao.login(user, connection ));
        User userResponse = MainFeatureDao.login(user, connection );
        if(userResponse.getId() == null){
            HelperView.message("Something wrong");
            return userResponse;
        }
        storage.setUsername( userResponse.getUsername() );
        storage.setId( userResponse.getId() );
        storage.setRole( userResponse.getRole() );
        HelperView.message("Login successfully...!");
        return userResponse;
    }
}
