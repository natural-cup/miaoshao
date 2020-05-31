package com.qsh.miaosha.service;

import com.qsh.miaosha.dao.UserDao;
import com.qsh.miaosha.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }
}
