package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.PersonServicePort;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.persistence.PersonPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonService implements PersonServicePort {

    private final PersonPersistencePort personPersistencePort;

    @Transactional(readOnly = true)
    @Override
    public PersonBo gerPersonById(String id) {
        return personPersistencePort.gerPersonById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PersonBo> getAllPerson() {
        return personPersistencePort.getAllPerson();
    }

    @Transactional
    @Override
    public String createPerson(PersonBo source) {
        return personPersistencePort.createPerson(source);
    }

    @Transactional
    @Override
    public void updatePerson(PersonBo source) {
        personPersistencePort.updatePerson(source);
    }

    @Transactional
    @Override
    public void deletePersonById(String id) {
        personPersistencePort.deletePersonById(id);
    }
}
