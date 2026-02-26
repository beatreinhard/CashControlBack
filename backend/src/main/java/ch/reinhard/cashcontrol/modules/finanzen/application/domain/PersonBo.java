package ch.reinhard.cashcontrol.modules.finanzen.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PersonBo {
    private String id;
    private String name;
    private String vorname;
    private LocalDate geburtsdatum;
    private String ahvnummer;
}
