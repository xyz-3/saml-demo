package mujina.sp;

import lombok.Builder;
import mujina.api.SpConfiguration;
import org.opensaml.common.SAMLException;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;

import javax.websocket.OnError;
import java.util.Collection;

public class SloSAMLProcessor extends SAMLProcessorImpl {
    private final SpConfiguration spConfiguration;

    public SloSAMLProcessor(Collection<SAMLBinding> bindings, SpConfiguration spConfiguration) {
        super(bindings);
        this.spConfiguration = spConfiguration;
    }

    @Override
    public SAMLMessageContext sendMessage(SAMLMessageContext samlContext, boolean sign) throws SAMLException, MetadataProviderException, MessageEncodingException {
        // send logout request to IDP
        Endpoint endpoint = samlContext.getPeerEntityEndpoint();

        SAMLBinding binding = getBinding(endpoint);

        samlContext.setLocalEntityId(spConfiguration.getEntityId());
        samlContext.getLocalEntityMetadata().setEntityID(spConfiguration.getEntityId());
        samlContext.getPeerEntityEndpoint().setLocation(spConfiguration.getIdpSLOServiceURL());

        return super.sendMessage(samlContext, spConfiguration.isNeedsSigning(), binding);
    }
}
