package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.persistence.PersonPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PersonPersistenceAdapter implements PersonPersistencePort {

    private final PersonJpaRepository personJpaRepository;

    @Override
    public PersonBo gerPersonById(String id) {
        return null;
    }

    @Override
    public List<PersonBo> getAllPerson() {
        return List.of();
    }

    @Override
    public String createPerson(PersonBo source) {
        return "";
    }

    @Override
    public void updatePerson(PersonBo source) {

    }

    @Override
    public void deletePersonById(String id) {

    }
}
