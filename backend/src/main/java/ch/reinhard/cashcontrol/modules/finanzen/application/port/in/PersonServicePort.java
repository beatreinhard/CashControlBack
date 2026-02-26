package ch.reinhard.cashcontrol.modules.finanzen.application.port.in;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;

import java.util.List;

public interface PersonServicePort {
    PersonBo getPersonById(String id);
    List<PersonBo> getAllPerson();
    String createPerson(PersonBo source);
    void updatePerson(PersonBo source);
    void deletePersonById(String id);
}
