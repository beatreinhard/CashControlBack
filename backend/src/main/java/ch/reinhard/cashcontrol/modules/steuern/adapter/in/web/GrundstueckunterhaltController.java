package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.port.in.GrundstueckunterhaltServicePort;
import ch.reinhard.cashcontrol.openapi.api.GrundstueckunterhaltControllerApi;
import ch.reinhard.cashcontrol.openapi.model.GrundstueckunterhaltDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.in.web.GrundstueckunterhaltWebMapper.*;

@RequiredArgsConstructor
@RestController
public class GrundstueckunterhaltController implements GrundstueckunterhaltControllerApi {

    private final GrundstueckunterhaltServicePort grundstueckunterhaltServicePort;

    @Override
    public ResponseEntity<String> createGrundstueckunterhalt(GrundstueckunterhaltDto grundstueckunterhaltDto) {
        var id = grundstueckunterhaltServicePort.createGrundstueckunterhalt(
                toGrundstueckunterhaltBo(grundstueckunterhaltDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteGrundstueckunterhaltById(String id) {
        grundstueckunterhaltServicePort.deleteGrundstueckunterhaltById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GrundstueckunterhaltDto> getGrundstueckunterhaltById(String id) {
        var grundstueckunterhaltDto =
                toGrundstueckunterhaltDto(grundstueckunterhaltServicePort.getGrundstueckunterhaltById(id));
        return ResponseEntity.ok(grundstueckunterhaltDto);
    }

    @Override
    public ResponseEntity<List<GrundstueckunterhaltDto>> getAllGrundstueckunterhalt() {
        return ResponseEntity.ok(
                toGrundstueckunterhaltDtoList(grundstueckunterhaltServicePort.getAllGrundstueckunterhalt()));
    }

    @Override
    public ResponseEntity<Void> updateGrundstueckunterhalt(String id, GrundstueckunterhaltDto grundstueckunterhaltDto) {
        var grundstueckunterhaltBo = toGrundstueckunterhaltBo(grundstueckunterhaltDto);
        grundstueckunterhaltBo.setId(id);
        grundstueckunterhaltServicePort.updateGrundstueckunterhalt(grundstueckunterhaltBo);
        return ResponseEntity.noContent().build();
    }
}
