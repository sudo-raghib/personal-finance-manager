package com.example.pfm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.*;
import java.io.IOException;
import java.util.Map;

public class JsonUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonUsernamePasswordFilter() {
        this.setAuthenticationSuccessHandler((req, res, auth) -> {

            var ctx = org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
            ctx.setAuthentication(auth);
            org.springframework.security.core.context.SecurityContextHolder.setContext(ctx);
            req.getSession(true)                       // creates JSESSIONID
                    .setAttribute(org.springframework.security.web.context
                            .HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, ctx);

            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json");
            res.getWriter().write("{\"message\":\"Login successful\"}");
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) {

        if ("application/json".equalsIgnoreCase(req.getContentType())) {
            try {
                Map<?, ?> body = mapper.readValue(req.getInputStream(), Map.class);
                String u = String.valueOf(body.get("username"));
                String p = String.valueOf(body.get("password"));
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(u, p);
                setDetails(req, auth);
                return getAuthenticationManager().authenticate(auth);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return super.attemptAuthentication(req, res);
    }
}
