
public class WeatherInfoRequestDTO {
private String pincode;
private String date;

public WeatherInfoRequestDTO() {}

public WeatherInfoRequestDTO(String pincode, String date) {
    this.pincode = pincode;
    this.date = date;
}

public String getPincode() {
    return pincode;
}

public void setPincode(String pincode) {
    this.pincode = pincode;
}

public String getDate() {
    return date;
}

public void setDate(String date) {
    this.date = date;
}

}