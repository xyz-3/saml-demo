package org.data.mujinaidpplatform.dao;

import org.data.mujinaidpplatform.Entity.User;
import org.data.mujinaidpplatform.repository.UserAuthoritiesRepository;
import org.data.mujinaidpplatform.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthoritiesRepository userAuthoritiesRepository;
    @Override
    public User getUser(String name, String password) {
        Optional<User> user = userRepository.findByNameAndPassword(name, password);
        if(!user.isPresent()){
            return null;
        }
        System.out.println("Get User.");
        List<String> authorities = userAuthoritiesRepository.findAllAuthoritiesByUserId(user.get().getId());
        user.get().setAuthorities(authorities);
        System.out.println("Get Authorities.");
        return user.get();
    }
}
