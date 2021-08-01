package repository;

import domain.User;

import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    public boolean createUser(User user);
    public User getUserByID(Long id);
    public User getUserByAccount(String account);
    public boolean deletedUser(Long id);
    public boolean updateUser(User user);
}
