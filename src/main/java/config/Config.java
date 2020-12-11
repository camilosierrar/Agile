package config;

public class Config {
    public static final int SPEED = 15;
    public static final int DURATION = 300;
    public enum Type_Request{
        PICK_UP,
        DELIVERY,
        DEPARTURE_ADDRESS
    };
    public static final int TIME_LIMIT = 20000; //in ms
}
