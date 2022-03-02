package saeger.oliver.osbdemo.config.filter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequiredRequestHeaderFilter extends OncePerRequestFilter {

    private final static String API_VERSION_HEADER_KEY = "X-Broker-API-Version";
    private final static String API_VERSION = "2.16";

    private final HandlerExceptionResolver resolver;

    public RequiredRequestHeaderFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String apiVersionHeader = request.getHeader(API_VERSION_HEADER_KEY);
        if (apiVersionHeader == null) {
            var exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, "API version header is required.");
            resolver.resolveException(request, response, null, exception);
            return;
        }

        if (!apiVersionHeader.equals(API_VERSION)) {
            var exception = new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Only API version " + API_VERSION + " is supported.");
            resolver.resolveException(request, response, null, exception);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
