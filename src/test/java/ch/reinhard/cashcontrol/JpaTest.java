package ch.reinhard.cashcontrol;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.JpaZahlungRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

// TODO müsste doch auch mit @DataJpaTest möglich sein!!!
// @DataJpaTest
@SpringBootTest
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
public class JpaTest {

    @Autowired
    private JpaZahlungRepository zahlungRepository;

    @Test
    public void testJpa() {
        zahlungRepository.findAll();
    }
}
