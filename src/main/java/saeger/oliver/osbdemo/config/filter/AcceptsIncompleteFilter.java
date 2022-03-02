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
public class AcceptsIncompleteFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;

    public AcceptsIncompleteFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String acceptsIncompleteParameter = request.getParameter("accepts_incomplete");

        if (Boolean.parseBoolean(acceptsIncompleteParameter)) {
            var exception = new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "For this demo asynchronous service broker operations are not allowed!");
            resolver.resolveException(request, response, null, exception);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
