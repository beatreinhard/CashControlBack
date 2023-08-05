package ch.reinhard.cashcontrol.modules.zahlung;

import ch.reinhard.cashcontrol.modules.zahlung.domain.JpaZahlungRepository;
import ch.reinhard.cashcontrol.modules.zahlung.service.ZahlungService;
import ch.reinhard.cashcontrol.modules.zahlung.service.api.ZahlungDetailsDto;
import ch.reinhard.cashcontrol.modules.zahlung.service.api.ZahlungUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = {ZahlungService.class})
@DataJpaTest
public class ZahlungServiceIT {

    @Autowired
    private ZahlungService zahlungService;

    @Autowired
    public JpaZahlungRepository zahlungRepository;

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
    public void searchZahlungen() {
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
}
