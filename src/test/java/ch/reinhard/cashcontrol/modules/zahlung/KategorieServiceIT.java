package ch.reinhard.cashcontrol.modules.zahlung;

import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence.JpaKategorieRepository;
import ch.reinhard.cashcontrol.modules.zahlung.service.KategorieEntityMapper;
import ch.reinhard.cashcontrol.modules.zahlung.service.KategorieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = {KategorieService.class})
@DataJpaTest
public class KategorieServiceIT {

    @Autowired
    private KategorieService kategorieService;

    @Autowired
    private JpaKategorieRepository kategorieRepository;

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
        var createdKategorie = kategorieRepository.getReferenceById(id);

        // WHEN
        var neueBezeichnung = "Versicherung";
        createdKategorie.update(neueBezeichnung);
        var updateKategorieDto = KategorieEntityMapper.toKategorieDto(createdKategorie);
        kategorieService.updateKategorie(updateKategorieDto);


        // THEN
        var updatedKategorie = kategorieRepository.getReferenceById(id);
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
        kategorieService.deleteZahlungById(id);
        var kategorieListAfterDelete = kategorieService.getAllKategorie();

        // THEN
        assertThat(kategorieListAfterDelete.isEmpty()).isTrue();
    }
}
