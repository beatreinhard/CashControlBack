package ch.reinhard.cashcontrol.core.service;

public class EnumMapper {

    public static <T extends Enum<T>> T convert(Enum<?> source, Class<T> targetEnumType) {
        if (!source.getClass().equals(targetEnumType.getEnclosingClass())) {
            throw new IllegalArgumentException("Die Enums müssen denselben Enum-Typ haben.");
        }

        return (T) source;
    }
}
