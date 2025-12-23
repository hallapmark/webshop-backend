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
    @Column(nullable = false, unique = true)
    private String slug;
    private String description_en;
    private String description_et;
    private double price;

    // parempoolne tähendab kas siin on ainsus või mitmus (List<Category> või Category)
    // vasakpoolne ytleb, kas saab taaskasutada (kas kategoorial võib mitu toodet olla)
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    // @OneToOne
    // @OneToMany
    // @ManyToOne
    // @ManyToMany


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
