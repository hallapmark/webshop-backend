package ee.markh.webshopbackend.seed.dto;

public record PersonSeed(
        String firstName,
        String lastName,
        String email,
        String password,
        String role
) {}
