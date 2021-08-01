package service;

import domain.User;

import java.util.HashMap;

public interface UserService {
    public boolean signUp(User user);
    public HashMap<String, String> login(User user) throws Exception;
    public boolean deleteAccount() throws Exception;
    public boolean updateAccount(User user) throws Exception;
    public User getUser() throws Exception;
}
