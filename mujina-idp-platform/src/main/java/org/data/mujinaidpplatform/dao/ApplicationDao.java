package org.data.mujinaidpplatform.dao;

import org.data.mujinaidpplatform.Entity.Application;

import java.io.IOException;
import java.util.List;

public interface ApplicationDao {
    String registerApplication(String name, String entityId, String baseUrl,
                               String acsLocationPath, String sloLocationPath, String cert) throws IOException;

    List<Application> getAllApplications();
}
