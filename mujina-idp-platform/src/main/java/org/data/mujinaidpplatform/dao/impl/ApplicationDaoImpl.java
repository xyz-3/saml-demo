package org.data.mujinaidpplatform.dao.impl;

import org.data.mujinaidpplatform.Entity.Application;
import org.data.mujinaidpplatform.repository.ApplicationRepository;
import org.data.mujinaidpplatform.dao.ApplicationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Repository
public class ApplicationDaoImpl implements ApplicationDao {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public String getSloUrlByEntityId(String entityId) {
        Application application = applicationRepository.findByEntityId(entityId);
        if(application == null){
            return null;
        }else{
            String base_url = application.getBaseUrl();
            String slo_location_path = application.getSloLocationPath();
            return base_url + slo_location_path;
        }
    }

    @Override
    public String registerApplication(String name, String entityId, String baseUrl,
                                      String acsLocationPath, String sloLocationPath) throws IOException {
        Application application = new Application();
        application.setName(name);
        application.setEntity_id(entityId);
        application.setBaseUrl(baseUrl);
        application.setAcsLocationPath(acsLocationPath);
        application.setSloLocationPath(sloLocationPath);
        applicationRepository.save(application);

        String metadata = new String(Files.readAllBytes(Paths.get("/Users/ycx/Documents/Projects/a&a/Mujina/mujina.local.idp.metadata.xml")));

        return metadata;
    }
}
