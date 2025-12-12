package ee.markh.webshopbackend.configuration;

import ee.markh.webshopbackend.service.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    // siin sees määrame kuhu pääseb niisama ligi, kuhu mitte
    @Bean
    public SecurityFilterChain configChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/persons").permitAll()
                        .requestMatchers(HttpMethod.GET, "/persons/**").hasRole("superadmin")
                        .requestMatchers(HttpMethod.DELETE, "/persons").hasRole("superadmin")
                        .requestMatchers(HttpMethod.GET, "/orders").hasRole("admin")
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("admin")
                        .requestMatchers(HttpMethod.DELETE, "/products/*").hasRole("admin")
                        .requestMatchers(HttpMethod.PUT, "/products").hasRole("admin")
                        .requestMatchers(HttpMethod.POST, "/categories").hasRole("admin")
                        .requestMatchers(HttpMethod.DELETE, "/categories").hasRole("admin")
                        .anyRequest().authenticated())
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                // halb:
//                .requestMatchers("persons").authenticated()
//                .requestMatchers("orders").authenticated()
//                .anyRequest().permitAll());
        // see on nagu else block, ei ole hea nii olulist asja else blocki panna nagu permit alli
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173")); // RestControllerist võin ära võtta @CrossOrigin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
