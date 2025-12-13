package ee.markh.webshopbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ee.markh.webshopbackend.entity.Person;
import ee.markh.webshopbackend.entity.PersonRole;
import ee.markh.webshopbackend.exception.ErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getHeader("Authorization") != null && request.getHeader("Authorization").startsWith("Bearer ")) {
            String token = request.getHeader("Authorization").replace("Bearer ", "");

            try {
                Person person = jwtService.getPersonIdAndRoleByToken(token);
                List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(person);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(person.getId(), "", grantedAuthorities)
                ); // õiguste list on see ArrayList. credentials poole peale läheb nimi (ei pea panema).
            } catch (ExpiredJwtException e) {
                // Token expired - write custom JSON response
                sendErrorResponse(response, "Token expired", HttpServletResponse.SC_UNAUTHORIZED);
                return; // Don't continue the filter chain
            } catch (Exception e) {
                // Other JWT errors (malformed, invalid signature, etc.)
                sendErrorResponse(response, "Invalid token", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private static List<GrantedAuthority> getGrantedAuthorities(Person person) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (person.getRole().equals(PersonRole.ADMIN)) {
            // Spring Security's .hasRole(bla) has a peculiarity: it expects the role to be typed "ROLE_bla"
            // https://docs.spring.io/spring-security/reference/servlet/authorization/architecture.html
            // "By default, role-based authorization rules include ROLE_ as a prefix."
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_admin");
            // i.e. we can then later retrieve with .hasRole("admin")
            grantedAuthorities.add(grantedAuthority);
        }
        if (person.getRole().equals(PersonRole.SUPERADMIN)) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_admin");
            GrantedAuthority grantedAuthority2 = new SimpleGrantedAuthority("ROLE_superadmin");
            grantedAuthorities.add(grantedAuthority);
            grantedAuthorities.add(grantedAuthority2);
        }
        return grantedAuthorities;
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(message);
        errorMessage.setStatus(status);
        errorMessage.setTimestamp(new Date());

        response.getWriter().write(objectMapper.writeValueAsString(errorMessage));
    }
}