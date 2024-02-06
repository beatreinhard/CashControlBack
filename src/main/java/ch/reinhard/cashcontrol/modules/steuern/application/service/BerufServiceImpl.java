package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.api.BerufService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Beruf;

import java.util.List;

public class BerufServiceImpl implements BerufService {

    @Override
    public List<Beruf> getAll() {
        return List.of();
    }
}
