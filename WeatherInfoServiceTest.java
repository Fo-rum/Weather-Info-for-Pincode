import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WeatherInfoServiceTest {

    private static final String PINCODE = "411014";
    private static final String DATE_STR = "2020-10-15";

    @Mock
    private WeatherInfoRepository weatherInfoRepository;

    @Mock
    private OkHttpClient client;

    @InjectMocks
    private WeatherInfoService weatherInfoService;

    @Test
    public void testGetWeatherInfo() throws Exception {
        // mock database response
        WeatherInfo mockWeatherInfo = new WeatherInfo(PINCODE, new Date(), "Cloudy", 25.0, 60.0);
        when(weatherInfoRepository.findByPincodeAndDate(eq(PINCODE), any(Date.class)))
                .thenReturn(mockWeatherInfo);

        // mock API response
        String mockLatLong = "18.518241,73.853501";
        WeatherResponse mockWeatherResponse = new WeatherResponse("Cloudy", 25.0, 60.0);
        when(weatherInfoService.getLatLongFromPincode(eq(PINCODE))).thenReturn(mockLatLong);
        when(weatherInfoService.getWeatherFromLatLong(eq("18.518241"), eq("73.853501")))
                .thenReturn(mockWeatherResponse);

        // invoke method
        WeatherInfoDTO result = weatherInfoService.getWeatherInfo(PINCODE, DATE_STR);

        // verify database save called
        verify(weatherInfoRepository, times(1)).save(any(WeatherInfo.class));

        // verify return value
        assertEquals(PINCODE, result.getPincode());
        assertEquals(DATE_STR, result.getDate());
        assertEquals(mockWeatherInfo.getDescription(), result.getDescription());
        assertEquals(mockWeatherInfo.getTemperature(), result.getTemperature(), 0.1);
        assertEquals(mockWeatherInfo.getHumidity(), result.getHumidity(), 0.1);
    }
}
