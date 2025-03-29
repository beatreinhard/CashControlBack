package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.PostgreSQLContainerInitializer;
import ch.reinhard.cashcontrol.config.TestConfig;
import ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web.AusgabeDto;
import ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web.AusgabeKategorieDto;
import ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.AusgabeKategorie;
import ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.JpaAusgabeRepository;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.AusgabeServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web.AusgabeWebMapper.toAusgabeBo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

// @ExtendWith(SpringExtension.class)
// @EnableAutoConfiguration
// @DataJpaTest

// @ContextConfiguration(classes = {ZahlungService.class})
// @TestPropertySource(properties = {"spring.datasource.url=jdbc:tc:postgresql://localhost:5432/cashcontrol"})

// https://medium.com/wearewaes/perform-integration-tests-anywhere-using-testcontainers-49d12219e3d

@SpringBootTest
// @EnableAutoConfiguration
// @DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
@Import(TestConfig.class)
public class AusgabeServiceIT {

    //    @Container
    //    static PostgreSQLContainer<?> postgresContainer = new
    // PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    //
    //    @DynamicPropertySource
    //    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
    //        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    //        registry.add("spring.datasource.username", postgresContainer::getUsername);
    //        registry.add("spring.datasource.password", postgresContainer::getPassword);
    //    }

    //@Autowired
    //private AusgabeService ausgabeService;

    @Autowired
    public JpaAusgabeRepository ausgabeRepository;

    @Autowired
    private AusgabeServicePort ausgabeService;

    @BeforeEach
    public void deleteAllAusgaben() {
        ausgabeRepository.deleteAll();
    }

    @Test
    public void createAusgabe() {
        // GIVEN
        var ausgabeDto = new AusgabeDto(
                null,
                null,
                LocalDate.now(),
                "Assura",
                AusgabeKategorieDto.GESUNDHEIT,
                "Krankenkasse",
                new BigDecimal("450.35"));

        // WHEN
        var id = ausgabeService.createAusgabe(toAusgabeBo(ausgabeDto));
        var createdAusgabe = ausgabeRepository.getReferenceById(id);

        // THEN
        assertEquals(id, createdAusgabe.getId());
    }

    @Test
    public void updateAusgabe() {
        // GIVEN
        var ausgabeDto = new AusgabeDto(
                null,
                null,
                LocalDate.now(),
                "Assura",
                AusgabeKategorieDto.GESUNDHEIT,
                "Krankenkasse",
                new BigDecimal("450.35"));
        var id = ausgabeService.createAusgabe(toAusgabeBo(ausgabeDto));
        ausgabeRepository.flush();

        // WHEN
        var createdAusgabeBo = ausgabeService.getAusgabeById(id);
        var ausgabeToUpdateBo = new AusgabeBo(
                createdAusgabeBo.id(),
                createdAusgabeBo.version(),
                createdAusgabeBo.datum(),
                createdAusgabeBo.empfaenger(),
                createdAusgabeBo.kategorie(),
                createdAusgabeBo.text(),
                new BigDecimal("455.55"));

        ausgabeService.updateAusgabe(ausgabeToUpdateBo);
        ausgabeRepository.flush();
        var updatedAusgabeAfterFlush = ausgabeService.getAusgabeById(id);

        // THEN
        assertEquals(new BigDecimal("455.55"), updatedAusgabeAfterFlush.betrag());
        assertEquals(0, createdAusgabeBo.version());
        assertEquals(1, updatedAusgabeAfterFlush.version());
    }

    @Test
    public void getAllAusgabe() {
        // GIVEN
        var ausgabeBo1 = new AusgabeBo(
                null,
                null,
                LocalDate.now(),
                "Assura",
                AusgabeKategorie.GESUNDHEIT,
                "Krankenkasse",
                new BigDecimal("450.35"));
        ausgabeService.createAusgabe(ausgabeBo1);
        var ausgabeBo2 = new AusgabeBo(
                null,
                null,
                LocalDate.now(),
                "Steuerverwaltung",
                AusgabeKategorie.STEUERN,
                "1. Rate",
                new BigDecimal("3400.05"));
        ausgabeService.createAusgabe(ausgabeBo2);

        // WHEN
        var ausgabeList = ausgabeService.getAllAusgabe();

        // THEN
        assertEquals(ausgabeList.size(), 2);
    }

    @Test
    public void getAusgabeById() {
        // GIVEN
        var empfaenger = "Assura";
        var ausgabeBo = new AusgabeBo(
                null,
                null,
                LocalDate.now(),
                empfaenger,
                AusgabeKategorie.GESUNDHEIT,
                "Krankenkasse",
                new BigDecimal("450.35"));
        ausgabeService.createAusgabe(ausgabeBo);
        var ausgabeList = ausgabeService.getAllAusgabe();

        // WHEN
        var id = ausgabeList.get(0).id();
        var ausgabeFoundById = ausgabeService.getAusgabeById(id);

        // THEN
        assertThat(ausgabeFoundById.id()).isEqualTo(id);
        assertThat(ausgabeFoundById.empfaenger()).isEqualTo(empfaenger);
    }

    @Test
    public void deleteZahlungById() {
        // GIVEN
        var empfaenger = "Assura";
        var ausgabeBo = new AusgabeBo(
                null,
                null,
                LocalDate.now(),
                empfaenger,
                AusgabeKategorie.GESUNDHEIT,
                "Krankenkasse",
                new BigDecimal("450.35"));
        ausgabeService.createAusgabe(ausgabeBo);
        var ausgabeList = ausgabeService.getAllAusgabe();

        // WHEN
        var id = ausgabeList.get(0).id();
        ausgabeService.deleteAusgabeById(id);
        var ausgabeListAfterDelete = ausgabeService.getAllAusgabe();

        // THEN
        assertThat(ausgabeListAfterDelete.isEmpty()).isTrue();
    }

    //    @Test
    //    public void searchZahlungenMitSuchkriterium() {
    //        // GIVEN
    //        var ausgabeDto1 = new AusgabeDto(null, null,
    //                LocalDate.now(), "Assura", AusgabeKategorieDto.GESUNDHEIT, "Krankenkasse", new
    // BigDecimal("450.35"));
    //        ausgabeService.createAusgabe(ausgabeDto1);
    //        var ausgabeDto2 = new AusgabeDto(null, null,
    //                LocalDate.now(), "Steuerverwaltung", AusgabeKategorieDto.STEUERN, "1. Rate", new
    // BigDecimal("3400.05"));
    //        ausgabeService.createAusgabe(ausgabeDto2);
    //
    //        // WHEN
    //        var searchEmpfaenger = "Reinhard";
    //        var zahlungList = ausgabeService.searchZahlungen(searchEmpfaenger);
    //
    //        // THEN
    //        assertEquals(1, zahlungList.size());
    //    }

}
