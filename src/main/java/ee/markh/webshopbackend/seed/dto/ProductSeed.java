package ee.markh.webshopbackend.seed.dto;

public record ProductSeed(
        String name,
        String slug,
        String description_en,
        String description_et,
        double price,
        CategoryRef category
) {
    public record CategoryRef(Long id) {}
}

