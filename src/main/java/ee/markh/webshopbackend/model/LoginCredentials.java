package ee.markh.webshopbackend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginCredentials {
    private String email;
    private String password;
}
