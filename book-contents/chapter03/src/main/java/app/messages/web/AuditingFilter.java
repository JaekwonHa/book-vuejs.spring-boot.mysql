package app.messages.web;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuditingFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.nanoTime();
        chain.doFilter(request, response);
        long elapsed = System.nanoTime() - start;
        HttpServletRequest req = (HttpServletRequest) request;
        logger.debug("Request[uri= " + req.getRequestURI() + ", method=" + req.getMethod() + "] complete in " + elapsed / 1000000 + " ms");
    }
}
