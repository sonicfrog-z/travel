package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * inquire user info by username
     * @param name
     * @return
     */
    public User findByUsername(String name);

    /**
     * save user info
     * @param user
     */
    public void save (User user);

    User fineByCode(String code);

    void updateStatus(User user);

    User findByUsernameAndPassword(String username, String password);
}
