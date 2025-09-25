package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.AusgabeServicePort;
import ch.reinhard.cashcontrol.openapi.api.AusgabeControllerApi;
import ch.reinhard.cashcontrol.openapi.model.AusgabeDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web.AusgabeWebMapper.*;


@AllArgsConstructor
@RestController
public class AusgabeController implements AusgabeControllerApi {

    private final AusgabeServicePort ausgabeServicePort;

    @Override
    public ResponseEntity<String> createAusgabe(AusgabeDto ausgabeDto) {
        var id = ausgabeServicePort.createAusgabe(toAusgabeBo(ausgabeDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteAusgabeById(String id) {
        ausgabeServicePort.deleteAusgabeById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AusgabeDto>> getAllAusgabe() {
        var ausgabeDtoList = toAusgabeDtoList(ausgabeServicePort.getAllAusgabe());
        return ResponseEntity.ok(ausgabeDtoList);
    }

    @Override
    public ResponseEntity<AusgabeDto> getAusgabeById(String id) {
        var ausgabeDto = toAusgabeDto(ausgabeServicePort.getAusgabeById(id));
        return ResponseEntity.ok(ausgabeDto);
    }

    @Override
    public ResponseEntity<Void> updateAusgabe(String id, AusgabeDto ausgabeDto) {
        AusgabeBo ausgabeBo = toAusgabeBo(ausgabeDto);
        ausgabeBo.id(id);
        ausgabeServicePort.updateAusgabe(ausgabeBo);
        return ResponseEntity.ok().build();
    }
}
