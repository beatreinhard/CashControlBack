package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "person", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonEntity {
    @NotNull
    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String vorname;

    @NotNull
    private LocalDate geburtsdatum;

    private String ahvnummer;
}
