package co.istad.util;

import co.istad.connection.ConnectionDb;
import co.istad.model.Role;
import co.istad.model.User;

import javax.swing.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

    public void removeBKP(){
        List<String> list = new ArrayList<>(9);
        String dir = "/Users/vanndeth/Desktop/Backup";
        File directory = new File(dir);
        File fList[] = directory.listFiles();
        if(fList.length != 0 && fList.length > 8){
            for (int i = 0; i < fList.length; i++){
                list.add(fList[i].getName());
            }

            String x = Collections.min(list);
            list.remove(Collections.max(list));

            String name = "/Users/vanndeth/Desktop/Backup" + x;
            File f = new File(name);
            f.delete();

            int m = 0;
            String recebe;
            String result;
            while (m < list.size()){
                recebe = list.get(m);
                result = recebe.replace(" ", " ".substring(1));

                m++;
                result = m + result;
                File file = new File("/Users/vanndeth/Desktop/Backup" + recebe);
                File file2 = new File("/Users/vanndeth/Desktop/Backup" + result);

                boolean success = file.renameTo(file2);
                if (!success){
                    JOptionPane.showMessageDialog(null, "No");

                }
            }
        }
    }
}
