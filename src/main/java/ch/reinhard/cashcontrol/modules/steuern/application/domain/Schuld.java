package ch.reinhard.cashcontrol.modules.steuern.application.domain;

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
public class Schuld {
    @Id
    private String id;

    @Version
    private Long version;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    private Integer jahr;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SchuldArt art;

    @NotNull
    private String glaeubiger;

    @NotNull
    private BigDecimal betrag;

    private BigDecimal zinsen;
}
