package ee.markh.webshopbackend.service;

import ee.markh.webshopbackend.entity.Person;
import ee.markh.webshopbackend.entity.PersonRole;
import ee.markh.webshopbackend.model.AuthToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    // access env variable JWT_SIGNING_KEY_BASE64. More Spring magic/auto-conversion
    @Value("${jwt.signing.key.base64}")
    private String jwtSigningKeyBase64;
    // base 64 formaadis peab jwtSigningKeyBase64 olema

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSigningKeyBase64));
    }

    private Date getExpiration() {
        return new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3));
    }

    public Person getPersonIdAndRoleByToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Long id = Long.parseLong(claims.getSubject());
        PersonRole role = PersonRole.valueOf(claims.get("role").toString());
        Person person = new Person();
        person.setId(id);
        person.setRole(role);
        return person;
    }

    public AuthToken generateToken(Person person) {
        Date expiration = getExpiration();
        AuthToken authToken = new AuthToken();
        authToken.setExpiration(expiration.getTime());
        authToken.setToken(Jwts
                .builder()
                .subject(person.getId().toString())
                .claim("role", person.getRole())
                .expiration(expiration)
                .signWith(getSecretKey())
                .compact());
        return authToken;
    }
}
