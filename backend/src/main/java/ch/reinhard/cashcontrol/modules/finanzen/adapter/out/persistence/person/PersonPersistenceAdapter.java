package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.person;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.persistence.PersonPersistencePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.person.PersonPersistenceMapper.*;

@Component
@RequiredArgsConstructor
public class PersonPersistenceAdapter implements PersonPersistencePort {

    private final PersonJpaRepository personRepository;

    @Override
    public PersonBo gerPersonById(String id) {
        var entity = personRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person nicht gefunden mit ID=" + id));
        return toPersonBo(entity);
    }

    @Override
    public List<PersonBo> getAllPerson() {
        var personList = personRepository.findAll();

        return toPersonBoList(personList);
    }

    @Override
    public String createPerson(PersonBo source) {
        var person = toPersonEntity(source);
        person.setId(IdGenerator.generateId());
        var personEntity = personRepository.save(person);
        return personEntity.getId();
    }

    @Override
    public void updatePerson(PersonBo source) {
        var personEntity = personRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Person nicht gefunden mit ID=" + source.getId()));
        personEntity.update(toPersonEntity(source));
        personRepository.save(personEntity);
    }

    @Override
    public void deletePersonById(String id) {
        personRepository.deleteById(id);
    }
}
