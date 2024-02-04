package ch.reinhard.cashcontrol.modules.steuern.api;

import ch.reinhard.cashcontrol.modules.steuern.impl.domain.Beruf;

import java.util.List;

public interface BerufService {
    public List<Beruf> getAll();
}
