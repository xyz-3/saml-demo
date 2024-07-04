package mujina.dao.impl;


import mujina.Entity.Application;
import mujina.dao.ApplicationDao;
import mujina.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
