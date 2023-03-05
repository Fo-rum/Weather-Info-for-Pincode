// WeatherInfo class (Entity class)
@Entity
public class WeatherInfo {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

private String pincode;

private Date date;

private String description;

private double temperature;

private int humidity;

public WeatherInfo(String pincode, Date date, String description, double temperature, int humidity) {
    this.pincode = pincode;
    this.date = date;
    this.description = description;
    this.temperature = temperature;
    this.humidity = humidity;
}

public WeatherInfo() {}

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getPincode() {
    return pincode;
}

public void setPincode(String pincode) {
    this.pincode = pincode;
}

public Date getDate() {
    return date;
}

public void setDate(Date date) {
    this.date = date;
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public double getTemperature() {
    return temperature;
}

public void setTemperature(double temperature) {
    this.temperature = temperature;
}

public int getHumidity() {
    return humidity;
}

public void setHumidity(int humidity) {
    this.humidity = humidity;
}

}

