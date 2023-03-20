package ch.reinhard.cashcontrol.modules.zahlung;

import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence.JpaKategorieRepository;
import ch.reinhard.cashcontrol.modules.zahlung.service.KategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

//@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = {KategorieService.class})
@DataJpaTest
public class KategorieIT {

    @Autowired
    private KategorieService kategorieService;

    @Autowired
    private JpaKategorieRepository kategorieRepository;

//    @Test
//    public void testStoreKategorie() {
//        // GIVEN
//        var kategorieDto = new KategorieDto(
//                IdGenerator.generateId(),
//                "Steuern"
//        );
//        var createdKategorie = kategorieService.createKategorie(kategorieDto);
//        assertEquals(createdKategorie.getBezeichnung(), "Steuern");
//
//        // WHEN
//        var id = createdKategorie.getId();
//        var kategorie = kategorieRepository.findById(id);
//
//        // THEN
//        assertEquals(kategorie.get().getId(), createdKategorie.getId());
//    }

//    @Test
//    public void testFindKategorieById() {
//        // GIVEN
//        var kategorieDto = new KategorieDto(
//                IdGenerator.generateId(),
//                "Steuern"
//        );
//        var createdKategorie = kategorieService.createKategorie(kategorieDto);
//        assertEquals(createdKategorie.getBezeichnung(), "Steuern");
//
//        // WHEN
//        var id = createdKategorie.getId();
//        var kategorie = kategorieService.findKategorieById(id);
//
//        // THEN
//        assertEquals(kategorie.getId(), createdKategorie.getId());
//    }
}
