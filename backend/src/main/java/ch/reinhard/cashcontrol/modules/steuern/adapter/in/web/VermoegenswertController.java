package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VermoegenswertServicePort;
import ch.reinhard.cashcontrol.openapi.api.VermoegenswertControllerApi;
import ch.reinhard.cashcontrol.openapi.model.VermoegenswertDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.in.web.VermoegenswertWebMapper.*;

@RequiredArgsConstructor
@RestController
public class VermoegenswertController implements VermoegenswertControllerApi {

    private final VermoegenswertServicePort vermoegenswertServicePort;

    @Override
    public ResponseEntity<String> createVermoegenswert(VermoegenswertDto vermoegenswertDto) {
        var id = vermoegenswertServicePort.createVermoegenswert(toVermoegenswertBo(vermoegenswertDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteVermoegenswertById(String id) {
        vermoegenswertServicePort.deleteVermoegenswertById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<VermoegenswertDto> getVermoegenswertById(String id) {
        var vermoegenswertDto = toVermoegenswertDto(vermoegenswertServicePort.getVermoegenswertById(id));
        return ResponseEntity.ok(vermoegenswertDto);
    }

    @Override
    public ResponseEntity<List<VermoegenswertDto>> getAllVermoegenswert() {
        return ResponseEntity.ok(toVermoegenswertDtoList(vermoegenswertServicePort.getAllVermoegenswert()));
    }

    @Override
    public ResponseEntity<Void> updateVermoegenswert(String id, VermoegenswertDto vermoegenswertDto) {
        var vermoegenswertBo = toVermoegenswertBo(vermoegenswertDto);
        vermoegenswertBo.setId(id);
        vermoegenswertServicePort.updateVermoegenswert(vermoegenswertBo);
        return ResponseEntity.noContent().build();
    }
}
