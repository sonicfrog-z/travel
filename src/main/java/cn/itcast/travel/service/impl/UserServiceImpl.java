package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    /**
     * register user
     *
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //find user by username
        User u = userDao.findByUsername(user.getUsername());
        //determine whether user is null
        if (u != null) {
            //username exist
            return false;
        }
        //set activation code
        user.setCode(UuidUtil.getUuid());
        //set activation status
        user.setStatus("N");
        //save user info
        userDao.save(user);

        //send activation email
        String content = "<a href='http://localhost/travel/activeUserServlet?code=" + user.getCode() + "'>Click to activate your account</a>";
        MailUtils.sendMail(user.getEmail(), content, "Active your account");
        return true;
    }

    /**
     * active user
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //find user by code
        User  user = userDao.fineByCode(code);
        if(user!=null){
            //call dao to change status
            userDao.updateStatus(user);
            return true;
        } else {
            return false;
        }

    }

    /**
     * login user
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
