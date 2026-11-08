package ch.reinhard.cashcontrol.modules.finanzen.application.service;

/**
 * Ein Import läuft bereits (z.B. Scheduler und manueller Aufruf gleichzeitig).
 */
public class ImportLaeuftBereitsException extends RuntimeException {

    public ImportLaeuftBereitsException() {
        super("Es läuft bereits ein Import");
    }
}
