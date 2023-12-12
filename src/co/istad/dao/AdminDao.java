package co.istad.dao;

import co.istad.model.User;

import java.util.List;

public interface AdminDao extends MainFeatureDao {
    public void backup();
    public void restore();
    public Long getUserCount();
    public Long getAdminCount();
    public Long getLibrarianCount();
    public Long getBooksCount();
    public List<User> getAllUser ();
    public List<User> getAllAdmin ();
    public List<User> getAllLibrarian();
    public void getReport();
    public void resetPassword(User user);
    public void disableAccount(User user);
    public void removeAccount(Long id);
    public void saveReportAsExcel();
}
