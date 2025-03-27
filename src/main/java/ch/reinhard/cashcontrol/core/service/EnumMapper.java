package ch.reinhard.cashcontrol.core.service;

public class EnumMapper {

    public static <T extends Enum<T>, U extends Enum<U>> U convert(T source, Class<U> targetEnumClass) {
        return Enum.valueOf(targetEnumClass, source.name());
    }

}
