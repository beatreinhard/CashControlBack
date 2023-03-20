package ch.reinhard.cashcontrol.core.persistence;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import static com.aventrix.jnanoid.jnanoid.NanoIdUtils.DEFAULT_ALPHABET;
import static com.aventrix.jnanoid.jnanoid.NanoIdUtils.DEFAULT_NUMBER_GENERATOR;

public class IdGenerator {

    private static String generateId(int size) {
        return NanoIdUtils.randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, size);
    }

    public static String generateId() {
        return generateId(15);
    }

}
