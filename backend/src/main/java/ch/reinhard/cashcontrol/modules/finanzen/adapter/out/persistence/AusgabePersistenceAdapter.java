package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.persistence.AusgabePersistencePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.AusgabePersistenceMapper.*;

@Component
@RequiredArgsConstructor
public class AusgabePersistenceAdapter implements AusgabePersistencePort {

    private final JpaAusgabeRepository ausgabeRepository;

    @Override
    public String createAusgabe(AusgabeBo source) {
        var ausgabe = toAusgabe(source);
        ausgabe.setId(IdGenerator.generateId());
        var ausgabeEntity = ausgabeRepository.save(ausgabe);
        return ausgabeEntity.getId();
    }

    @Override
    public AusgabeBo getAusgabeById(String id) {
        var entity = ausgabeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ausgabe nicht gefunden mit ID=" + id));
        return toAusgabeBo(entity);
    }

    @Override
    public List<AusgabeBo> getAllAusgabe() {
        var ausgabeList = ausgabeRepository.findAll();

        return toAusgabeBoList(ausgabeList);
    }


    @Override
    public void updateAusgabe(AusgabeBo source) {
        var ausgabeEntity = ausgabeRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Ausgabe nicht gefunden mit ID=" + source.getId()));
        ausgabeEntity.update(toAusgabe(source));
        ausgabeRepository.save(ausgabeEntity);
    }

    @Override
    public void deleteAusgabeById(String id) {
        ausgabeRepository.deleteById(id);
    }
}
