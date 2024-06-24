package mujina.dao;

import mujina.Entity.User;

import java.util.List;

public interface UserDao {
    User getUser(String name, String password);
}
