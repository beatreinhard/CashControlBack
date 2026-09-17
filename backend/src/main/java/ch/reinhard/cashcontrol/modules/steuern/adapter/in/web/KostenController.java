package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.port.in.KostenServicePort;
import ch.reinhard.cashcontrol.openapi.api.KostenControllerApi;
import ch.reinhard.cashcontrol.openapi.model.KostenDto;
import ch.reinhard.cashcontrol.openapi.model.KostenViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.in.web.KostenWebMapper.*;

@RequiredArgsConstructor
@RestController
public class KostenController implements KostenControllerApi {

    private final KostenServicePort kostenServicePort;

    @Override
    public ResponseEntity<String> createKosten(KostenDto kostenDto) {
        var id = kostenServicePort.createKosten(toKostenBo(kostenDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteKostenById(String id) {
        kostenServicePort.deleteKostenById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<KostenViewDto>> getAllKostenView(Integer jahr) {
        List<KostenViewDto> kostenViewDtoList;
        if (jahr != null) {
            kostenViewDtoList = toKostenViewDtoList(kostenServicePort.getKostenViewByJahr(jahr));
        } else {
            kostenViewDtoList = toKostenViewDtoList(kostenServicePort.getKostenView());
        }
        return ResponseEntity.ok(kostenViewDtoList);
    }

    @Override
    public ResponseEntity<KostenDto> getKostenById(String id) {
        var kostenDto = toKostenDto(kostenServicePort.getKostenById(id));
        return ResponseEntity.ok(kostenDto);
    }

    @Override
    public ResponseEntity<Void> updateKosten(String id, KostenDto kostenDto) {
        var kostenBo = toKostenBo(kostenDto);
        kostenBo.setId(id);
        kostenServicePort.updateKosten(kostenBo);
        return ResponseEntity.noContent().build();
    }
}
