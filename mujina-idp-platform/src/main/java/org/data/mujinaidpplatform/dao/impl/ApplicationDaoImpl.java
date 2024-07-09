package org.data.mujinaidpplatform.dao.impl;

import org.data.mujinaidpplatform.Entity.Application;
import org.data.mujinaidpplatform.repository.ApplicationRepository;
import org.data.mujinaidpplatform.dao.ApplicationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ApplicationDaoImpl implements ApplicationDao {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public String registerApplication(String name, String entityId, String baseUrl,
                                      String acsLocationPath, String sloLocationPath) throws IOException {
        Application application = new Application();
        application.setName(name);
        application.setEntity_id(entityId);
        application.setBaseUrl(baseUrl);
        application.setAcsLocationPath(acsLocationPath);
        application.setSloLocationPath(sloLocationPath);
        application.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        applicationRepository.save(application);

        String metadata = new String(Files.readAllBytes(Paths.get("/Users/ycx/Documents/Projects/a&a/Mujina/mujina.local.idp.metadata.xml")));

        return metadata;
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }
}
