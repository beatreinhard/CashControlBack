package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.person;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PersonPersistenceMapper {
    public static PersonEntity toPersonEntity(PersonBo source) {
        return new PersonEntity(
                source.getId(),
                source.getName(),
                source.getVorname(),
                source.getGeburtsdatum(),
                source.getAhvnummer());
    }

    public static PersonBo toPersonBo(PersonEntity source) {
        return new PersonBo(
                source.getId(),
                source.getName(),
                source.getVorname(),
                source.getGeburtsdatum(),
                source.getAhvnummer());
    }

    public static List<PersonBo> toPersonBoList(List<PersonEntity> personEntityList) {
        return personEntityList.stream().map(PersonPersistenceMapper::toPersonBo).toList();
    }
}
