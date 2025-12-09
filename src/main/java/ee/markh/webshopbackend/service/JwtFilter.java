package ee.markh.webshopbackend.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Authorization") != null && request.getHeader("Authorization").startsWith("Bearer ")) {
            String token = request.getHeader("Authorization").replace("Bearer ", "");
//            Long id = null;
//            try {
            Long id = jwtService.getPersonIdByToken(token);
//           } catch (Exception e) {
////                response.setStatus(400);
////                filterChain.doFilter(request, response);
//               throw new RuntimeException("Token expired");
//           }
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(id, "", new ArrayList<>()));
        }
        // permit all hyppab ifist yle lihtsalt
        // sellega laseks igale poole ligi jälle
        //SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("", "", new ArrayList<>()));
        filterChain.doFilter(request, response); // originaal tagasi
    }
}
