package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.openapi.model.PersonDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PersonWebMapper {

    public static PersonBo toPersonBo(PersonDto source) {
        return new PersonBo(
                source.getId(),
                source.getName(),
                source.getVorname(),
                source.getGeburtsdatum(),
                source.getAhvnummer());
    }

    public static PersonDto toPersonDto(PersonBo source) {
        PersonDto personDto = new PersonDto(
                source.getName(),
                source.getVorname(),
                source.getGeburtsdatum());

        personDto.setId(source.getId());
        personDto.setAhvnummer(source.getAhvnummer());
        return personDto;
    }

    public static List<PersonDto> toPersonDtoList(List<PersonBo> personBoList) {
        return personBoList.stream().map(PersonWebMapper::toPersonDto).toList();
    }
}
