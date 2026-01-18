package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.port.in.SchuldServicePort;
import ch.reinhard.cashcontrol.openapi.api.SchuldControllerApi;
import ch.reinhard.cashcontrol.openapi.model.SchuldDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SchuldController implements SchuldControllerApi {

    private final SchuldServicePort schuldServicePort;

    @Override
    public ResponseEntity<String> createSchuld(SchuldDto schuldDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteSchuldById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<List<SchuldDto>> getAllSchuld() {
        return null;
    }

    @Override
    public ResponseEntity<SchuldDto> getSchuldById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateSchuld(String id, SchuldDto schuldDto) {
        return null;
    }
}
