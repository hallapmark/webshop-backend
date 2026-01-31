package ee.markh.webshopbackend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String slug;
    private String description_en;
    private String description_et;
    private double price;
    private Long categoryId;
    private String categoryName;
    private String imageUrl;
}
