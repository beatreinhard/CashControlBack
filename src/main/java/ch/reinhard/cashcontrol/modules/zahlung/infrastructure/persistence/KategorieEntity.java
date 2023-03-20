package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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

    @Version
    Long version;

    String bezeichnung;

    public KategorieEntity(String id, String bezeichnung) {
        this.id = id;
        this.bezeichnung = bezeichnung;
    }

    public KategorieEntity update(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        return this;
    }
}
