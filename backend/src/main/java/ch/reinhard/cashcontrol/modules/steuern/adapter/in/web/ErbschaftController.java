package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.port.in.ErbschaftServicePort;
import ch.reinhard.cashcontrol.openapi.api.ErbschaftControllerApi;
import ch.reinhard.cashcontrol.openapi.model.ErbschaftDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.in.web.ErbschaftWebMapper.*;

@RequiredArgsConstructor
@RestController
public class ErbschaftController implements ErbschaftControllerApi {

    private final ErbschaftServicePort erbschaftServicePort;

    @Override
    public ResponseEntity<String> createErbschaft(ErbschaftDto erbschaftDto) {
        var id = erbschaftServicePort.createErbschaft(toErbschaftBo(erbschaftDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteErbschaftById(String id) {
        erbschaftServicePort.deleteErbschaftById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ErbschaftDto> getErbschaftById(String id) {
        var erbschaftDto = toErbschaftDto(erbschaftServicePort.getErbschaftById(id));
        return ResponseEntity.ok(erbschaftDto);
    }

    @Override
    public ResponseEntity<List<ErbschaftDto>> getAllErbschaft() {
        return ResponseEntity.ok(toErbschaftDtoList(erbschaftServicePort.getAllErbschaft()));
    }

    @Override
    public ResponseEntity<Void> updateErbschaft(String id, ErbschaftDto erbschaftDto) {
        var erbschaftBo = toErbschaftBo(erbschaftDto);
        erbschaftBo.setId(id);
        erbschaftServicePort.updateErbschaft(erbschaftBo);
        return ResponseEntity.noContent().build();
    }
}
