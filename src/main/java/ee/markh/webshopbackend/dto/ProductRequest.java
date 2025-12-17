package ee.markh.webshopbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String slug;
    private String description_en;
    private String description_et;
    private double price;
    private Long categoryId;
}