package org.data.mujinaidpplatform.dao;

import java.io.IOException;

public interface ApplicationDao {
    String getSloUrlByEntityId(String entityId);

    String registerApplication(String name, String entityId, String baseUrl,
                               String acsLocationPath, String sloLocationPath) throws IOException;
}
