package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.port.in.BerufServicePort;
import ch.reinhard.cashcontrol.openapi.api.BerufControllerApi;
import ch.reinhard.cashcontrol.openapi.model.BerufDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.in.web.BerufWebMapper.*;

@RequiredArgsConstructor
@RestController
public class BerufController implements BerufControllerApi {

    private final BerufServicePort berufServicePort;

    @Override
    public ResponseEntity<String> createBeruf(BerufDto berufDto) {
        var id = berufServicePort.createBeruf(toBerufBo(berufDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteBerufById(String id) {
        berufServicePort.deleteBerufById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<BerufDto> getBerufById(String id) {
        var berufDto = toBerufDto(berufServicePort.getBerufById(id));
        return ResponseEntity.ok(berufDto);
    }

    @Override
    public ResponseEntity<List<BerufDto>> getAllBeruf() {
        return ResponseEntity.ok(toBerufDtoList(berufServicePort.getAllBeruf()));
    }

    @Override
    public ResponseEntity<Void> updateBeruf(String id, BerufDto berufDto) {
        var berufBo = toBerufBo(berufDto);
        berufBo.setId(id);
        berufServicePort.updateBeruf(berufBo);
        return ResponseEntity.noContent().build();
    }
}
