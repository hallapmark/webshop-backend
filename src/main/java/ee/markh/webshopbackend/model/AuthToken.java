package ee.markh.webshopbackend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuthToken {
    private String token;
    private Long expiration;
}
