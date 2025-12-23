package ee.markh.webshopbackend.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.markh.webshopbackend.entity.Person;
import ee.markh.webshopbackend.entity.PersonRole;
import ee.markh.webshopbackend.repository.PersonRepository;
import ee.markh.webshopbackend.seed.dto.PersonSeed;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

@Component
@Profile("seed")
public class PersonSeeder {

    private static final Logger log = LoggerFactory.getLogger(PersonSeeder.class);
    private static final String ENV_B64 = "PERSONS_JSON_B64";
    private static final String CLASSPATH_RESOURCE = "seed/persons.json";

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public PersonSeeder(PersonRepository personRepository,
                        PasswordEncoder passwordEncoder,
                        ObjectMapper objectMapper) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void seed() {
        try {
            // quick guard: if persons already exist, don't touch anything
            if (personRepository.count() > 0) {
                log.info("Skipping PersonSeeder: persons already exist");
                return;
            }

            InputStream is = locatePersonsInputStream();
            if (is == null) {
                log.info("PersonSeeder: no persons.json available (env or classpath). Skipping persons seeding.");
                return;
            }

            List<PersonSeed> seeds = objectMapper.readValue(is, new TypeReference<List<PersonSeed>>() {});
            log.info("PersonSeeder: found {} entries to seed", seeds.size());

            for (PersonSeed seed : seeds) {
                if (seed.email() == null) {
                    log.warn("Skipping seed entry with null email: {}", seed);
                    continue;
                }
                if (personRepository.findByEmail(seed.email()) != null) {
                    log.info("User with email {} already exists — skipping", seed.email());
                    continue;
                }

                Person person = new Person();
                person.setFirstName(seed.firstName());
                person.setLastName(seed.lastName());
                person.setEmail(seed.email());

                // safe enum parsing: default to CUSTOMER if missing/invalid
                try {
                    person.setRole(PersonRole.valueOf(seed.role()));
                } catch (Exception e) {
                    log.warn("Invalid or missing role '{}' for {}, defaulting to CUSTOMER", seed.role(), seed.email());
                    person.setRole(PersonRole.CUSTOMER);
                }

                // encode the plaintext password (must be present in JSON for seeding)
                String rawPassword = seed.password();
                if (rawPassword == null || rawPassword.isBlank()) {
                    log.warn("Skipping {}: missing password", seed.email());
                    continue;
                }
                person.setPassword(passwordEncoder.encode(rawPassword));

                personRepository.save(person);
                log.info("Seeded person {}", seed.email());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to seed persons", e);
        }
    }

    /**
     * Locate input stream for persons JSON:
     * 1) If env PERSONS_JSON_B64 exists, decode and return stream
     * 2) Else, try classpath resource "seed/persons.json"
     * 3) If neither found, return null
     */
    private InputStream locatePersonsInputStream() {
        try {
            String b64 = System.getenv(ENV_B64);
            if (b64 != null && !b64.isBlank()) {
                // You can encode a json list of persons into a suitable format for the env variable
                // with BASE64_STRING=$(base64 -i src/main/resources/seed/persons.json | tr -d '\n')
                // echo $BASE64_STRING
                // (modify -i flag as appropriate)
                // Then copy that into the backend environment as the key for PERSONS_JSON_B64
                log.info("PersonSeeder: reading persons JSON from environment variable {}", ENV_B64);
                byte[] decoded = Base64.getDecoder().decode(b64);
                return new ByteArrayInputStream(decoded);
            }

            ClassPathResource r = new ClassPathResource(CLASSPATH_RESOURCE);
            if (r.exists()) {
                log.info("PersonSeeder: reading persons JSON from classpath resource {}", CLASSPATH_RESOURCE);
                return r.getInputStream();
            }

            return null;
        } catch (Exception e) {
            log.warn("PersonSeeder: error locating persons.json: {}", e.getMessage(), e);
            return null;
        }
    }
}
