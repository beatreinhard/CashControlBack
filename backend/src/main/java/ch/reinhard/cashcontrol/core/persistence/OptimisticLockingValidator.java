package ch.reinhard.cashcontrol.core.persistence;

import org.springframework.dao.OptimisticLockingFailureException;

import static java.lang.String.format;

public class OptimisticLockingValidator {

    public static void validateOptimisticLocking(Long versionForUpdate, Long currentVersionOnDatabase, Class entity) {
        if (currentVersionOnDatabase > versionForUpdate) {
            throw new OptimisticLockingFailureException(
                    format("Entity {0} could not be updated since it was mutated by someone else.", entity.getName()));
        }
    }
}
