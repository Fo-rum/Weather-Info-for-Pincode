class WeatherInfoException extends Exception {
public WeatherInfoException(String message) {
super(message);
}

public WeatherInfoException(String message, Throwable cause) {
    super(message, cause);
}