package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import ch.reinhard.cashcontrol.openapi.api.VergabungControllerApi;
import ch.reinhard.cashcontrol.openapi.model.VergabungDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.in.web.VergabungWebMapper.*;

@RequiredArgsConstructor
@RestController
public class VergabungController implements VergabungControllerApi {
    private final VergabungServicePort vergabungServicePort;


    @Override
    public ResponseEntity<String> createVergabung(VergabungDto vergabungDto) {
        var id = vergabungServicePort.createVergabung(toVergabungBo(vergabungDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<VergabungDto>> getAllVergabungen() {
        var vergabungDtoList = toVergabungDtoList(vergabungServicePort.getAllVergabung());
        return ResponseEntity.ok(vergabungDtoList);
    }

    @Override
    public ResponseEntity<VergabungDto> getVergabungById(String id) {
        var bo = vergabungServicePort.getVergabungById(id);
        return ResponseEntity.ok(toVergabungDto(bo));
    }
}
