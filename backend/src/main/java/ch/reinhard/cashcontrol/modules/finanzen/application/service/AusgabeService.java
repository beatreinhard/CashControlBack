package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.AusgabeServicePort;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.persistence.AusgabePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AusgabeService implements AusgabeServicePort {

    private final AusgabePersistencePort ausgabePersistencePort;


    public String createAusgabe(AusgabeBo source) {
        return ausgabePersistencePort.createAusgabe(source);
    }

    public AusgabeBo getAusgabeById(String id) {
        return ausgabePersistencePort.getAusgabeById(id);
    }

    public List<AusgabeBo> getAllAusgabe() {
        return ausgabePersistencePort.getAllAusgabe();
    }

    public void updateAusgabe(AusgabeBo source) {
        ausgabePersistencePort.updateAusgabe(source);
    }

    public void deleteAusgabeById(String id) {
        ausgabePersistencePort.deleteAusgabeById(id);
    }

}
