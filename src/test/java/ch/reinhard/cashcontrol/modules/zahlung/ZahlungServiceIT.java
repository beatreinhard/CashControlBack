package ch.reinhard.cashcontrol.modules.zahlung;

import ch.reinhard.cashcontrol.PostgreSQLContainerInitializer;
import ch.reinhard.cashcontrol.modules.zahlung.domain.JpaZahlungRepository;
import ch.reinhard.cashcontrol.modules.zahlung.service.ZahlungService;
import ch.reinhard.cashcontrol.modules.zahlung.service.api.ZahlungDetailsDto;
import ch.reinhard.cashcontrol.modules.zahlung.service.api.ZahlungUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


// @ExtendWith(SpringExtension.class)
// @EnableAutoConfiguration
// @DataJpaTest

// @ContextConfiguration(classes = {ZahlungService.class})
// @TestPropertySource(properties = {"spring.datasource.url=jdbc:tc:postgresql://localhost:5432/cashcontrol"})


// https://medium.com/wearewaes/perform-integration-tests-anywhere-using-testcontainers-49d12219e3d



@SpringBootTest
//@EnableAutoConfiguration
//@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
public class ZahlungServiceIT {

//    @Container
//    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
//
//    @DynamicPropertySource
//    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgresContainer::getUsername);
//        registry.add("spring.datasource.password", postgresContainer::getPassword);
//    }

    @Autowired
    private ZahlungService zahlungService;

    @Autowired
    public JpaZahlungRepository zahlungRepository;

    @BeforeEach
    public void deleteAllZahlungen() {
        zahlungRepository.deleteAll();
    }

    @Test
    public void createZahlung() {
        // GIVEN
        var zahlungDto = new ZahlungDetailsDto(
                LocalDate.now(),
                "Beat Reinhard, Frutigen",
                "K1",
                "Lohn",
                Long.valueOf("150060")
        );

        // WHEN
        var id = zahlungService.createZahlung(zahlungDto);
        var createdZahlung = zahlungRepository.getReferenceById(id);

        // THEN
        assertEquals(id, createdZahlung.getId());
    }

    @Test
    public void updateZahlung() {
        // GIVEN
        var zahlungDto = new ZahlungDetailsDto(
                LocalDate.now(),
                "Beat Reinhard, Frutigen",
                "K1",
                "Lohn",
                Long.valueOf("150060")
        );
        var id = zahlungService.createZahlung(zahlungDto);
        zahlungRepository.flush();


        // WHEN
        var createdZahlung = zahlungService.getZahlungById(id);
        var zahlungDetails = new ZahlungDetailsDto(
           createdZahlung.datum(),
           "Neu",
           "Lohn",
           createdZahlung.kategorieId(),
           createdZahlung.betrag()
        );

        var zahlungUpdateDto = new ZahlungUpdateDto(
                createdZahlung.id(),
                createdZahlung.version(),
                zahlungDetails
        );
        zahlungService.updateZahlung(zahlungUpdateDto);
        zahlungRepository.flush();
        var updatedZahlungAfterFlush = zahlungService.getZahlungById(id);

        // THEN
        assertEquals("Neu", updatedZahlungAfterFlush.empfaenger());
        assertEquals(0, createdZahlung.version());
        assertEquals(1, updatedZahlungAfterFlush.version());
    }


    @Test
    public void getAllZahlung() {
        // GIVEN
        var zahlungDto1 = new ZahlungDetailsDto(
                LocalDate.now(),
                "Hans Kummer, Bern",
                "K2",
                "Lohn",
                Long.valueOf("159995")
        );
        var zahlungDto2 = new ZahlungDetailsDto(
                LocalDate.now(),
                "Beat Reinhard, Frutigen",
                "K1",
                "Lohn",
                Long.valueOf("8310")
        );
        zahlungService.createZahlung(zahlungDto1);
        zahlungService.createZahlung(zahlungDto2);

        // WHEN
        var zahlungList = zahlungService.getAllZahlung();

        // THEN
        assertEquals(zahlungList.size(), 2);
    }

    @Test
    public void getZahlungById() {
        // GIVEN
        var empfaenger = "Hans Kummer, Bern";
        var zahlungDto = new ZahlungDetailsDto(
                LocalDate.now(),
                empfaenger,
                "K2",
                "Lohn",
                Long.valueOf("159995")
        );
        zahlungService.createZahlung(zahlungDto);
        var zahlungList = zahlungService.getAllZahlung();

        // WHEN
        var id = zahlungList.get(0).id();
        var zahlungFoundById = zahlungService.getZahlungById(id);

        // THEN
        assertThat(zahlungFoundById.id()).isEqualTo(id);
        assertThat(zahlungFoundById.empfaenger()).isEqualTo(empfaenger);
    }

    @Test
    public void deleteZahlungById() {
        // GIVEN
        var empfaenger = "Hans Kummer, Bern";
        var zahlungDto = new ZahlungDetailsDto(
                LocalDate.now(),
                empfaenger,
                "K2",
                "Lohn",
                Long.valueOf("159995")
        );
        zahlungService.createZahlung(zahlungDto);
        var zahlungList = zahlungService.getAllZahlung();

        // WHEN
        var id = zahlungList.get(0).id();
        zahlungService.deleteZahlungById(id);
        var zahlungListAfterDelete = zahlungService.getAllZahlung();

        // THEN
        assertThat(zahlungListAfterDelete.isEmpty()).isTrue();
    }

    @Test
    public void searchZahlungenMitSuchkriterium() {
        // GIVEN
        var zahlungDto1 = new ZahlungDetailsDto(
                LocalDate.now(),
                "Hans Kummer, Bern",
                "K2",
                "Lohn",
                Long.valueOf("159995")
        );
        var zahlungDto2 = new ZahlungDetailsDto(
                LocalDate.now(),
                "Beat Reinhard, Frutigen",
                "K1",
                "Lohn",
                Long.valueOf("8310")
        );
        zahlungService.createZahlung(zahlungDto1);
        zahlungService.createZahlung(zahlungDto2);

        // WHEN
        var searchEmpfaenger = "Reinhard";
        var zahlungList = zahlungService.searchZahlungen(searchEmpfaenger);

        // THEN
        assertEquals(zahlungList.size(), 1);
    }

    @Test
    public void searchZahlungenMitView() {
        // GIVEN
        var zahlungDto1 = new ZahlungDetailsDto(
                LocalDate.now(),
                "Hans Kummer, Bern",
                "K2",
                "Lohn",
                Long.valueOf("159995")
        );
        var zahlungDto2 = new ZahlungDetailsDto(
                LocalDate.now(),
                "Beat Reinhard, Frutigen",
                "K1",
                "Lohn",
                Long.valueOf("8310")
        );
        zahlungService.createZahlung(zahlungDto1);
        zahlungService.createZahlung(zahlungDto2);

        // WHEN
        var zahlungList = zahlungService.searchZahlungen("Reinhard");

        // THEN
        assertEquals(zahlungList.size(), 1);
    }
}
