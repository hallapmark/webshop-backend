package ee.markh.webshopbackend.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.markh.webshopbackend.entity.Category;
import ee.markh.webshopbackend.repository.CategoryRepository;
import ee.markh.webshopbackend.seed.dto.CategorySeed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Profile("seed")
@Transactional
public class CategorySeeder {

    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager em;

    public CategorySeeder(CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }

    public void seed() throws IOException {
        // skip seeding if categories already exist
        if (categoryRepository.count() > 0) return;

        InputStream is = new ClassPathResource("seed/categories.json").getInputStream();
        List<CategorySeed> seeds = objectMapper.readValue(is, new TypeReference<List<CategorySeed>>() {});

        for (CategorySeed seed : seeds) {
            // native SQL insert
            em.createNativeQuery("INSERT INTO category (id, name) VALUES (:id, :name)")
                    .setParameter("id", seed.id())
                    .setParameter("name", seed.name())
                    .executeUpdate();
        }

        // flush changes to the database
        em.flush();

        // reset the PostgreSQL sequence to avoid conflicts with auto-generated IDs
        resetSequence();
    }

    private void resetSequence() {
        em.createNativeQuery(
                "SELECT setval(pg_get_serial_sequence('category', 'id'), " +
                        "COALESCE((SELECT MAX(id) FROM category), 0))"
        ).getSingleResult();
    }
}
