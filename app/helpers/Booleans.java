package helpers;


public class Booleans {

    public static boolean valueOrFalse(Boolean value) {
        return value == null ? false : value.booleanValue();
    }
}
