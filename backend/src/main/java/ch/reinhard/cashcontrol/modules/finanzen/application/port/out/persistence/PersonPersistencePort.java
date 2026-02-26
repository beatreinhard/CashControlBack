package ch.reinhard.cashcontrol.modules.finanzen.application.port.out.persistence;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;

import java.util.List;

public interface PersonPersistencePort {
    PersonBo gerPersonById(String id);
    List<PersonBo> getAllPerson();
    String createPerson(PersonBo source);
    void updatePerson(PersonBo source);
    void deletePersonById(String id);
}
