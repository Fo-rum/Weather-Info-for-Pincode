// WeatherResponse class
public class WeatherResponse {
private final String description;
private final double temperature;
private final int humidity;

public WeatherResponse(String description, double temperature, int humidity) {
    this.description = description;
    this.temperature = temperature;
    this.humidity = humidity;
}

public String getDescription() {
    return description;
}

public double getTemperature() {
    return temperature;
}

public int getHumidity() {
    return humidity;
}

}