package org.data.mujinaidpplatform.dao;


import org.data.mujinaidpplatform.Entity.User;

import java.util.List;

public interface UserDao {
    User getUser(String name, String password);

    List<User> getAllUsers();
}
