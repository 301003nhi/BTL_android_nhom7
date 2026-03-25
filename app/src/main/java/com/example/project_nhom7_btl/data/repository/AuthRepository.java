package com.example.project_nhom7_btl.data.repository;

import com.example.project_nhom7_btl.data.local.dao.UserDao;
import com.example.project_nhom7_btl.data.local.entity.UserEntity;

public class AuthRepository {

    private final UserDao userDao;

    public AuthRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean register(String username, String password, String fullName) {
        UserEntity existingUser = userDao.getUserByUsername(username);
        if (existingUser != null) {
            return false;
        }
        userDao.insertUser(new UserEntity(username, password, fullName));
        return true;
    }

    public UserEntity login(String username, String password) {
        return userDao.login(username, password);
    }
}