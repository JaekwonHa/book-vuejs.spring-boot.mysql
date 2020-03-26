package com.taskagile.domain.common.security;

import com.taskagile.domain.model.user.SimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final static Logger log = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("Acess to `" + httpServletRequest.getRequestURI() + "` denied");
        }

        if (httpServletRequest.getRequestURI().startsWith("/api/")) {
            if (httpServletRequest.getUserPrincipal() instanceof SimpleUser) {
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            httpServletResponse.sendRedirect("/login");
        }
    }
}
