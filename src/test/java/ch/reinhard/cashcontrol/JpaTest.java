package ch.reinhard.cashcontrol;

import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence.JpaZahlungRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@EnableAutoConfiguration
//@ContextConfiguration(classes = {JpaZahlungRepository .class})
@DataJpaTest
public class JpaTest {

    @Autowired
    private JpaZahlungRepository zahlungRepository;

    @Disabled
    @Test
    public void testJpa() {
        zahlungRepository.findAll();
    }
}
