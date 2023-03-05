@Service
public class WeatherInfoService {

private final WeatherInfoRepository weatherInfoRepository;
private final OkHttpClient client;
private final String googleMapsApiKey;
private final String openWeatherApiKey;

@Autowired
public WeatherInfoService(WeatherInfoRepository weatherInfoRepository,
                          OkHttpClient client,
                          @Value("${google.maps.api.key}") String googleMapsApiKey,
                          @Value("${openweather.api.key}") String openWeatherApiKey) {
    this.weatherInfoRepository = weatherInfoRepository;
    this.client = client;
    this.googleMapsApiKey = googleMapsApiKey;
    this.openWeatherApiKey = openWeatherApiKey;
}

public WeatherInfoDTO getWeatherInfo(String pincode, String dateStr) throws WeatherInfoException {
    // Validate inputs
    if (pincode == null || pincode.isEmpty()) {
        throw new WeatherInfoException("Pincode cannot be null or empty");
    }
    if (dateStr == null || dateStr.isEmpty()) {
        throw new WeatherInfoException("Date string cannot be null or empty");
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date;
    try {
        date = LocalDate.parse(dateStr, formatter);
    } catch (DateTimeParseException e) {
        throw new WeatherInfoException("Invalid date format: " + dateStr);
    }

    WeatherInfo weatherInfo = weatherInfoRepository.findByPincodeAndDate(pincode, date);
    if (weatherInfo != null) {
        return new WeatherInfoDTO(weatherInfo.getPincode(), weatherInfo.getDate(),
                weatherInfo.getDescription(), weatherInfo.getTemperature(), weatherInfo.getHumidity());
    }

    String latLong = getLatLongFromPincode(pincode);
    String[] latLongArray = latLong.split(",");
    String lat = latLongArray[0];
    String lon = latLongArray[1];
    WeatherResponse weatherResponse = getWeatherFromLatLong(lat, lon);
    WeatherInfo newWeatherInfo = new WeatherInfo(pincode, date, weatherResponse.getDescription(),
            weatherResponse.getTemperature(), weatherResponse.getHumidity());

    weatherInfoRepository.save(newWeatherInfo);

    return new WeatherInfoDTO(newWeatherInfo.getPincode(), newWeatherInfo.getDate(),
            newWeatherInfo.getDescription(), newWeatherInfo.getTemperature(), newWeatherInfo.getHumidity());
}

private String getLatLongFromPincode(String pincode) throws WeatherInfoException {
    String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + pincode + "&key=" + googleMapsApiKey;
    Request request = new Request.Builder()
            .url(url)
            .build();
    try (Response response = client.newCall(request).execute()) {
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            JSONArray results = json.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject result = results.getJSONObject(0);
                JSONObject geometry = result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                Double lat = location.getDouble("lat");
                Double lng = location.getDouble("lng");
                return lat.toString() + "," + lng.toString();
            } else {
                throw new WeatherInfoException("No results found for pincode: " + pincode);
            }
        } else {
            throw new WeatherInfoException("Unable to get latitude and longitude from pincode: " + pincode);
        }
    } catch (IOException e) {
        throw new WeatherInfoException("Error occurred while getting latitude and longitude from pincode: " + pincode);
    }
}

private WeatherResponse getWeatherFromLatLong(String lat, String lon) throws WeatherInfoException {
    String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon +
"&units=metric&appid=" + openWeatherApiKey;
Request request = new Request.Builder()
.url(url)
.build();
try (Response response = client.newCall(request).execute()) {
if (response.isSuccessful()) {
String responseBody = response.body().string();
JSONObject json = new JSONObject(responseBody);
JSONArray weatherArray = json.getJSONArray("weather");
JSONObject weatherObject = weatherArray.getJSONObject(0);
String description = weatherObject.getString("description");
JSONObject main = json.getJSONObject("main");
Double temperature = main.getDouble("temp");
Double humidity = main.getDouble("humidity");
return new WeatherResponse(description, temperature, humidity);
} else {
throw new WeatherInfoException("Unable to get weather information for latitude: " + lat + " and longitude: " + lon);
}
} catch (IOException e) {
throw new WeatherInfoException("Error occurred while getting weather information for latitude: " + lat + " and longitude: " + lon);
}
}
