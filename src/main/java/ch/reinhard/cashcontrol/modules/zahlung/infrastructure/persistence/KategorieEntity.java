package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="kategorie", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KategorieEntity {

    @Id
    String id;
    String bezeichnung;
}
