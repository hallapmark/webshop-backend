package ee.markh.webshopbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String description_est;
    private double price;

    // @OneToOne
    // @OneToMany
    // @ManyToOne
    // @ManyToMany

    // parempoolne tähendab kas siin on ainsus või mitmus (List<Category> või Category)
    // vasakpoolne ytleb, kas saab taaskasutada (kas kategoorial võib mitu toodet olla)
    @ManyToOne
    private Category category;

    // @OneToOne näide
    // private Ingredients ingredients;

    // @OneToMany
    // Ei taha et keegi teine võtaks.
    // private List<>
    // vbl nt:
    // private List<EAN> eanCodes; USA kood, EU kood

    // @ManyToMany
    // private List<Producer> vendor
}
