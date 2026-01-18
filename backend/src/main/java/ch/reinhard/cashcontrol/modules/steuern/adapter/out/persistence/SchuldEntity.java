package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "schuld", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
@Getter
@Setter
public class SchuldEntity {
    @Id
    private String id;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    private Integer jahr;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SchuldArtEntity art;

    @NotNull
    private String glaeubiger;

    @NotNull
    private BigDecimal betrag;

    private BigDecimal zinsen;

    public void update(SchuldEntity schuldEntity) {
        jahr = schuldEntity.jahr;
        art = schuldEntity.art;
        glaeubiger = schuldEntity.glaeubiger;
        betrag = schuldEntity.betrag;
        zinsen = schuldEntity.zinsen;
    }
}
