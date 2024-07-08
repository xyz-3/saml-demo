package org.data.mujinaidpplatform.dao.impl;

import org.data.mujinaidpplatform.Entity.UserApplication;
import org.data.mujinaidpplatform.dao.UserApplicationDao;
import org.data.mujinaidpplatform.repository.ApplicationRepository;
import org.data.mujinaidpplatform.repository.UserApplicationRepository;
import org.data.mujinaidpplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class UserApplicationDaoImpl implements UserApplicationDao {
    @Autowired
    private UserApplicationRepository userApplicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public boolean addUserApplication(String username, String entityId) {
        Integer userId = userRepository.findIdByName(username);
        Integer applicationId = applicationRepository.findIdByEntityId(entityId);
        if(userId == null || applicationId == null){
            return false;
        }
        UserApplication userApplication = new UserApplication();
        userApplication.setUserId(userId);
        userApplication.setApplicationId(applicationId);
        userApplication.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userApplicationRepository.save(userApplication);
        return true;
    }
}
