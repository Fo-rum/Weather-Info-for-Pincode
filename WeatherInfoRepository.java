// WeatherInfoRepository interface
@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {
WeatherInfo findByPincodeAndDate(String pincode, Date date);
}