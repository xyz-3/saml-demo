package org.data.mujinaidpplatform.dao;


import org.data.mujinaidpplatform.Entity.User;

public interface UserDao {
    User getUser(String name, String password);
}
