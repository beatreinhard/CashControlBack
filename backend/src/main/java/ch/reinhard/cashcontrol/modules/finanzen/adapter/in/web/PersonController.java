package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.PersonServicePort;
import ch.reinhard.cashcontrol.openapi.api.PersonControllerApi;
import ch.reinhard.cashcontrol.openapi.model.PersonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web.PersonWebMapper.*;

@RequiredArgsConstructor
@RestController
public class PersonController implements PersonControllerApi {

    private final PersonServicePort personServicePort;

    @Override
    public ResponseEntity<String> createPerson(PersonDto personDto) {
        var id = personServicePort.createPerson(toPersonBo(personDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deletePersonById(String id) {
        personServicePort.deletePersonById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PersonDto>> getAllPerson() {
        var personDtoList = toPersonDtoList(personServicePort.getAllPerson());
        return ResponseEntity.ok(personDtoList);
    }

    @Override
    public ResponseEntity<PersonDto> getPersonById(String id) {
        var personDto = toPersonDto(personServicePort.getPersonById(id));
        return ResponseEntity.ok(personDto);
    }

    @Override
    public ResponseEntity<Void> updatePerson(String id, PersonDto personDto) {
        // TODO Sicherstellen, dass die ID aus dem Pfad verwendet wird

        PersonBo personBo = toPersonBo(personDto);
        personBo.setId(id);
        personServicePort.updatePerson(personBo);
        return ResponseEntity.noContent().build();
    }
}
