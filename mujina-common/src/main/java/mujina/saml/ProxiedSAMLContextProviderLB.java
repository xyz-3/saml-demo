package mujina.saml;

import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.springframework.security.saml.context.SAMLContextProviderLB;
import org.springframework.security.saml.context.SAMLMessageContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/*
继承自SAMLContextProviderLB，用于处理在负载均衡代理环境中的 SAML 上下文配置。
 */
public class ProxiedSAMLContextProviderLB extends SAMLContextProviderLB {

    public ProxiedSAMLContextProviderLB(URI uri) {
        super();
        setServerName(uri.getHost());
        setScheme(uri.getScheme());
        setContextPath("");
        if (uri.getPort() > 0) {
            setIncludeServerPortInRequestURL(true);
            setServerPort(uri.getPort());
        }
    }

    /*
    重写方法以填充通用的 SAML 上下文信息。在处理通过代理服务器进行的 SAML 认证时特别有用。
     */
    @Override
    public void populateGenericContext(HttpServletRequest request, HttpServletResponse response, SAMLMessageContext context)
            throws MetadataProviderException {
        super.populateGenericContext(request, response, context);
    }

}
