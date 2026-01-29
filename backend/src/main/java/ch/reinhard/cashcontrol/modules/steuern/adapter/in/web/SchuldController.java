package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.SchuldServicePort;
import ch.reinhard.cashcontrol.openapi.api.SchuldControllerApi;
import ch.reinhard.cashcontrol.openapi.model.SchuldDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.in.web.SchuldWebMapper.*;

@RequiredArgsConstructor
@RestController
public class SchuldController implements SchuldControllerApi {

    private final SchuldServicePort schuldServicePort;

    @Override
    public ResponseEntity<String> createSchuld(SchuldDto schuldDto) {
        var id = schuldServicePort.createSchuld(toSchuldBo(schuldDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteSchuldById(String id) {
        schuldServicePort.deleteSchuldById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<SchuldDto> getSchuldById(String id) {
        var schuldDto = toSchuldDto(schuldServicePort.getSchuldById(id));
        return ResponseEntity.ok(schuldDto);
    }

    @Override
    public ResponseEntity<List<SchuldDto>> getSchulden(Integer jahr) {
        List<SchuldBo> schuldBoList;
        if (jahr != null) {
            schuldBoList = schuldServicePort.getSchuldByJahr(jahr);
        } else {
            schuldBoList = schuldServicePort.getAllSchuld();
        }
        return ResponseEntity.ok(toSchuldDtoList(schuldBoList));
    }

    @Override
    public ResponseEntity<Void> updateSchuld(String id, SchuldDto schuldDto) {

        // TODO Sicherstellen, dass die ID aus dem Pfad verwendet wird

        SchuldBo schuldBo = toSchuldBo(schuldDto);
        schuldBo.setId(id);
        schuldServicePort.updateSchuld(schuldBo);
        return ResponseEntity.noContent().build();
    }
}
