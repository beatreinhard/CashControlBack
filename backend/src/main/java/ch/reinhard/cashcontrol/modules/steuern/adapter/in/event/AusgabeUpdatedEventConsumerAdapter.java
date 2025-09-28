package ch.reinhard.cashcontrol.modules.steuern.adapter.in.event;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeUpdatedEvent;
import org.springframework.context.ApplicationListener;

public class AusgabeUpdatedEventConsumerAdapter implements ApplicationListener<AusgabeUpdatedEvent> {

    @Override
    public void onApplicationEvent(AusgabeUpdatedEvent event) {

    }
}
