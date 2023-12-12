package co.istad.util;

import co.istad.connection.ConnectionDb;
import co.istad.model.Role;
import co.istad.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminUtil {
    private Integer option;
    public Integer getOption() {
        return option;
    }
    public void setOption(Integer option) {
        this.option = option;
    }
    private final Connection connection;
    public AdminUtil(){
        connection = ConnectionDb.getConnection();
    }
    public Long getCount(String query) {
        Long count = 0L;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                count += rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
    public List<User> getUsers(List<User> userResponse, String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                User userRes = new User();
                Role role = new Role();
                userRes.setId(rs.getLong("id"));
                userRes.setUsername(rs.getString("username"));
                userRes.setEmail(rs.getString("email"));
                role.setRole( RoleEnum.valueOf(rs.getString("role")) );
                userRes.setRole(role);
                userRes.setDisable(rs.getBoolean("is_Disable"));
                userResponse.add(userRes);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userResponse;
    }
}
