package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.AusgabeServicePort;
import ch.reinhard.cashcontrol.openapi.model.AusgabeDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web.AusgabeWebMapper.toAusgabeBo;
import static ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web.AusgabeWebMapper.toAusgabeDtoList;


// TODO: rename zu AusgabeController
// TODO: implemenieren der restlichen Methoden anhand der alten Klasse AusgabeController


@AllArgsConstructor
@RestController
public class AusgabeControllerApi implements ch.reinhard.cashcontrol.openapi.api.AusgabeControllerApi {

    private final AusgabeServicePort ausgabeServicePort;

    @Override
    public ResponseEntity<String> createAusgabe(AusgabeDto ausgabeDto) {
        var id = ausgabeServicePort.createAusgabe(toAusgabeBo(ausgabeDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteAusgabeById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<List<AusgabeDto>> getAllAusgabe() {
        var ausgabeDtoList = toAusgabeDtoList(ausgabeServicePort.getAllAusgabe());
        return new ResponseEntity<>(ausgabeDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AusgabeDto> getAusgabeById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateAusgabe(String id, AusgabeDto ausgabeDto) {
        return null;
    }
}
