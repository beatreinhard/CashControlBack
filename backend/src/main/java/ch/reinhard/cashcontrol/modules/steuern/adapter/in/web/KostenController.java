package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.api.KostenService;
import ch.reinhard.cashcontrol.openapi.api.KostenControllerApi;
import ch.reinhard.cashcontrol.openapi.model.KostenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class KostenController implements KostenControllerApi {
    private final KostenService kostenService;

    @Override
    public ResponseEntity<String> createKosten(KostenDto kostenDto) {
        var id = kostenService.createKosten(kostenDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteKostenById(String id) {
        kostenService.deleteKostenById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<KostenDto>> getKosten(Integer jahr) {
        List<KostenDto> kostenDtoList;
        if (jahr != null) {
           kostenDtoList = kostenService.getKostenByJahr(jahr);
        } else {
            kostenDtoList = kostenService.getAllKosten();
        }
        return ResponseEntity.ok(kostenDtoList);
    }

    @Override
    public ResponseEntity<KostenDto> getKostenById(String id) {
        var kostenDto = kostenService.getKostenById(id);
        return ResponseEntity.ok(kostenDto);
    }

    @Override
    public ResponseEntity<Void> updateKosten(String id, KostenDto kostenDto) {
        // TODO Sicherstellen, dass die ID aus dem Pfad verwendet wird

        kostenDto.setId(id);
        kostenService.updateKosten(kostenDto);
        return ResponseEntity.noContent().build();
    }
}
