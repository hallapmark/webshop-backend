package ee.markh.webshopbackend.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("seed")
public class SeedCoordinator implements CommandLineRunner {

    private final CategorySeeder categorySeeder;
    private final ProductSeeder productSeeder;
    private final PersonSeeder personSeeder;

    public SeedCoordinator(CategorySeeder categorySeeder,
                           ProductSeeder productSeeder,
                           PersonSeeder personSeeder) {
        this.categorySeeder = categorySeeder;
        this.productSeeder = productSeeder;
        this.personSeeder = personSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
        categorySeeder.seed();
        // categories first. Products assume existing categories
        productSeeder.seed();
        personSeeder.seed();
    }
}
