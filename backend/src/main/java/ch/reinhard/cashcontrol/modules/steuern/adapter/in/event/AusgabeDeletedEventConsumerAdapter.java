package ch.reinhard.cashcontrol.modules.steuern.adapter.in.event;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeDeletedEvent;
import org.springframework.context.ApplicationListener;

public class AusgabeDeletedEventConsumerAdapter implements ApplicationListener<AusgabeDeletedEvent> {

    @Override
    public void onApplicationEvent(AusgabeDeletedEvent event) {

    }
}
