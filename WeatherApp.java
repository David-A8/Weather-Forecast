import org.json.JSONObject;
import java.util.Scanner;
import java.net.URL;
import java.io.IOException;

public class WeatherApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String apikey = "37de911389e24decb42165002252203";
        System.out.println("Weather Forecast App\n");
        System.out.println("Enter a city: ");
        String city = scanner.nextLine().replace(" ", "%20");
        String urlString = "http://api.weatherapi.com/v1/current.json?key=" + apikey + "&q=" + city;

        try {
            URL url = new URL(urlString);
            Scanner scanner2 = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();

            while (scanner2.hasNext()) {
                response.append(scanner2.nextLine());
            }
            scanner2.close();
            JSONObject jsonResponse = new JSONObject(response.toString());

            if (jsonResponse.has("error")) {
                System.out.println("The city you entered does not exist");
                return;
            }
            JSONObject current = jsonResponse.getJSONObject("current");
            JSONObject location = jsonResponse.getJSONObject("location");

            // Extract data from Json
            String cityName = location.getString("name");
            String region = location.getString("region");
            String country = location.getString("country");

            double temperatureC = current.getDouble("temp_c"); // Temperature in Celsius
            double temperatureF = current.getDouble("temp_f"); // Temperature in Fahrenheit
            String condition = current.getJSONObject("condition").getString("text"); // Weather condition
            double humidity = current.getDouble("humidity"); // Humidity %
            double windSpeed = current.getDouble("wind_kph"); // Wind speed in km/h
            double feelsLikeC = current.getDouble("feelslike_c"); // Feels like temperature in Celsius
            double feelsLikeF = current.getDouble("feelslike_f"); // Feels like temperature in Fahrenheit

            // Print all the data
            System.out.println("\nWeather Report for " + cityName + ", " + region + ", " + country);
            System.out.println(
                    "------------------------------------------------------------------------------");
            System.out.println("Condition: " + condition);
            System.out.println("Temperature: " + temperatureC + "°C (" + temperatureF + "°F)");
            System.out.println("Feels Like: " + feelsLikeC + "°C (" + feelsLikeF + "°F)");
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Wind Speed: " + windSpeed + " km/h");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}