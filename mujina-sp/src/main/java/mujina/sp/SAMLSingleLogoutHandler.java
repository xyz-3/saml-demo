package mujina.sp;

import mujina.api.SpConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.stereotype.Component;

@Component
public class SAMLSingleLogoutHandler {
    @Autowired
    private SpConfiguration spConfiguration;

    @Autowired
    @Qualifier("metadata")
    private MetadataManager metadataManager;

    @Autowired
    private SAMLProcessor samlProcessor;
}
