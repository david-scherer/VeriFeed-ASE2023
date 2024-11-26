package at.ac.tuwien.verifeed.core;

import at.ac.tuwien.verifeed.core.util.DatabaseInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = {DatabaseInitializer.class})
class CoreApplicationTests {

    @Test
    void contextLoads() {
    }

}
