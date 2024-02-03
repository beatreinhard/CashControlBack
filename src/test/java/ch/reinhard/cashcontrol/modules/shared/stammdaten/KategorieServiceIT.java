package ch.reinhard.cashcontrol.modules.shared.stammdaten;

import ch.reinhard.cashcontrol.PostgreSQLContainerInitializer;
import ch.reinhard.cashcontrol.modules.shared.stammdaten.domain.JpaKategorieRepository;
import ch.reinhard.cashcontrol.modules.shared.stammdaten.service.KategorieEntityMapper;
import ch.reinhard.cashcontrol.modules.shared.stammdaten.service.KategorieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@ExtendWith(SpringExtension.class)
//@EnableAutoConfiguration
//@ContextConfiguration(classes = {KategorieService.class})
//@DataJpaTest

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
public class KategorieServiceIT {

    @Autowired
    private KategorieService kategorieService;

    @Autowired
    private JpaKategorieRepository kategorieRepository;

    @BeforeEach
    public void deleteAllZahlungen() {
        kategorieRepository.deleteAll();
    }
    @Test
    public void createKategorie() {
        // GIVEN
        var bezeichnung = "Steuerverwaltung";

        // WHEN
        var id = kategorieService.createKategorie(bezeichnung);
        var createdKategorie = kategorieRepository.getReferenceById(id);

        // THEN
        assertEquals(id, createdKategorie.getId());
    }

    @Test
    public void updateKategorie() {
        // GIVEN
        var bezeichnung = "Steuerverwaltung";
        var id = kategorieService.createKategorie(bezeichnung);
        var createdKategorie = kategorieRepository.findById(id).orElseThrow();
        kategorieRepository.flush();

        // WHEN
        var neueBezeichnung = "Versicherung";
        createdKategorie.update(neueBezeichnung);
        var updateKategorieDto = KategorieEntityMapper.toKategorieDto(createdKategorie);
        kategorieService.updateKategorie(updateKategorieDto);
        kategorieRepository.flush();

        // THEN
        var updatedKategorie = kategorieRepository.findById(id).orElseThrow();
        assertThat(updatedKategorie.getBezeichnung()).isEqualTo(neueBezeichnung);
    }

    @Test
    public void getAllKategorie() {
        // GIVEN
        var kategorieBezeichnung1 = "Versicherung";
        var kategorieBezeichnung2 = "Auto";
        kategorieService.createKategorie(kategorieBezeichnung1);
        kategorieService.createKategorie(kategorieBezeichnung2);

        // WHEN
        var kategorieList = kategorieService.getAllKategorie();

        // THEN
        assertEquals(kategorieList.size(), 2);
    }

    @Test
    public void getKategorieById() {
        // GIVEN
        var kategorieBezeichnung = "Versicherung";
        kategorieService.createKategorie(kategorieBezeichnung);
        var kategorieList = kategorieService.getAllKategorie();

        // WHEN
        var id = kategorieList.get(0).id();
        var kategorieFoundById = kategorieService.getKategorieById(id);

        // THEN
        assertThat(kategorieFoundById.id()).isEqualTo(id);
        assertThat(kategorieFoundById.bezeichnung()).isEqualTo(kategorieBezeichnung);
    }

    @Test
    public void deleteZahlungById() {
        // GIVEN
        var kategorieBezeichnung = "Versicherung";
        kategorieService.createKategorie(kategorieBezeichnung);
        var kategorieList = kategorieService.getAllKategorie();

        // WHEN
        var id = kategorieList.get(0).id();
        kategorieService.deleteKategorieById(id);
        var kategorieListAfterDelete = kategorieService.getAllKategorie();

        // THEN
        assertThat(kategorieListAfterDelete.isEmpty()).isTrue();
    }
}
