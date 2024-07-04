package mujina.idp;

import mujina.api.IdpConfiguration;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.core.AuthnRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ForceAuthnFilter extends OncePerRequestFilter {

    private SAMLMessageHandler samlMessageHandler;

    private IdpConfiguration idpConfiguration;

    public ForceAuthnFilter(SAMLMessageHandler samlMessageHandler, IdpConfiguration idpConfiguration) {
        this.samlMessageHandler = samlMessageHandler;
        this.idpConfiguration = idpConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        // 处理用户登录请求（已跳转idp域名）
        if (servletPath == null || (!servletPath.endsWith("SingleSignOnService") &&
                !servletPath.endsWith("SingleLogoutService")) ||
                request.getMethod().equalsIgnoreCase("GET")) {
            chain.doFilter(request, response);
            return;
        }

        // 处理sp端发来的saml request
        SAMLMessageContext messageContext;
        try {
            messageContext = samlMessageHandler.extractSAMLMessageContext(request, response,
                    request.getMethod().equalsIgnoreCase("POST"));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        idpConfiguration.setIssuer(messageContext.getInboundMessageIssuer());
        AuthnRequest authnRequest = (AuthnRequest) messageContext.getInboundSAMLMessage();
        if (authnRequest.isForceAuthn()) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        chain.doFilter(request, response);
    }
}
