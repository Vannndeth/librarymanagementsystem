package co.istad.service;

import co.istad.model.User;

import java.util.List;

public interface AdminService extends MainFeatureService {
    public void backUp();
    public void restore();
    public Long getUserCount();
    public Long getAdminCount();
    public Long getLibrariansCount();
    public Long getBooksCount();
    public List<User> getAllUser ();
    public List<User> getAllAdmin ();
    public List<User> getAllLibrarians ();
    public void getReport();
    public void resetPassword();
    public void disableAccount();
    public User removeAccount();
    public void saveReportAsExcel();
}
