package ch.reinhard.cashcontrol.modules.steuern.impl.service;

import ch.reinhard.cashcontrol.modules.steuern.api.BerufService;
import ch.reinhard.cashcontrol.modules.steuern.impl.domain.Beruf;

import java.util.List;

public class BerufServiceImpl implements BerufService {

    @Override
    public List<Beruf> getAll() {
        return List.of();
    }
}
