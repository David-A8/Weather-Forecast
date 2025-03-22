import org.json.JSONObject;
import java.util.Scanner;
import java.net.URL;

public class WeatherApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String apikey = "37de911389e24decb42165002252203";
        System.out.println("Weather Forecast App");

        while (true) {
            // Displays a main menu
            System.out.println("\nSelect and option: ");
            System.out.println("1. Get Current Weather");
            System.out.println("2. Get 3-Day Forecast");
            System.out.println("3. Exit");

            String choice = scanner.nextLine().trim();
            if (choice.equals("3")) {
                System.out.println("Exiting the app...");
                break;
            }

            switch (choice) {
                case "1":
                    getWeather(scanner, apikey); // Calls the function to get current weather
                    break;
                case "2":
                    getForecast(scanner, apikey); // Calls the function to get 3-day forecast
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
        scanner.close();
    }

    private static void getWeather(Scanner scanner, String apikey) {
        System.out.println("\nCurrent Weather\n");
        System.out.println("Enter a city: ");
        String city = scanner.nextLine().replace(" ", "%20");
        String urlString = "http://api.weatherapi.com/v1/current.json?key=" + apikey + "&q=" + city;
        try {
            URL url = new URL(urlString);
            StringBuilder response = new StringBuilder();
            try (Scanner scanner2 = new Scanner(url.openStream())) {
                while (scanner2.hasNext()) {
                    response.append(scanner2.nextLine());
                }
            }
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("error")) {
                System.out.println("The city you entered does not exist");
                return;
            }
            JSONObject current = jsonResponse.getJSONObject("current");
            JSONObject location = jsonResponse.getJSONObject("location");
            // Extract data from JSON
            String cityName = location.getString("name");
            String region = location.getString("region");
            String country = location.getString("country");
            double temperatureC = current.getDouble("temp_c");
            double temperatureF = current.getDouble("temp_f");
            String condition = current.getJSONObject("condition").getString("text");
            double humidity = current.getDouble("humidity");
            double windSpeed = current.getDouble("wind_kph");
            String windDirection = current.getString("wind_dir");
            double uvIndex = current.getDouble("uv");
            double feelsLikeC = current.getDouble("feelslike_c");
            double feelsLikeF = current.getDouble("feelslike_f");
            // Display weather data
            System.out.println("\nWeather Report for " + cityName + ", " + region + ", " + country);
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Condition: " + condition);
            System.out.println("Temperature: " + temperatureC + "°C (" + temperatureF + "°F)");
            System.out.println("Feels Like: " + feelsLikeC + "°C (" + feelsLikeF + "°F)");
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Wind Speed: " + windSpeed + " km/h" + windDirection);
            System.out.println("UV Index: " + uvIndex);
        } catch (Exception e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
        }
    }

    private static void getForecast(Scanner scanner, String apikey) {
        System.out.println("\n3-day forecast\n");
        System.out.println("Enter a city: ");
        String city = scanner.nextLine().replace(" ", "%20");

        String urlString = "http://api.weatherapi.com/v1/forecast.json?key=" + apikey + "&q=" + city + "&days=3";

        try {
            URL url = new URL(urlString);
            StringBuilder response = new StringBuilder();

            try (Scanner scanner2 = new Scanner(url.openStream())) {
                while (scanner2.hasNext()) {
                    response.append(scanner2.nextLine());
                }
            }

            JSONObject jsonResponse = new JSONObject(response.toString());

            if (jsonResponse.has("error")) {
                System.out.println("The city you entered does not exist.");
                return;
            }

            JSONObject location = jsonResponse.getJSONObject("location");
            String cityName = location.getString("name");
            String region = location.getString("region");
            String country = location.getString("country");
            JSONObject forecast = jsonResponse.getJSONObject("forecast");
            JSONObject forecastDay1 = forecast.getJSONArray("forecastday").getJSONObject(0);
            JSONObject forecastDay2 = forecast.getJSONArray("forecastday").getJSONObject(1);
            JSONObject forecastDay3 = forecast.getJSONArray("forecastday").getJSONObject(2);

            // Extract data for 3 days
            System.out.println("\n3-Day Weather Forecast for " + cityName + ", " + region + ", " + country);
            displayForecast(forecastDay1);
            displayForecast(forecastDay2);
            displayForecast(forecastDay3);

        } catch (Exception e) {
            System.out.println("Error fetching forecast data: " + e.getMessage());
        }
    }

    private static void displayForecast(JSONObject forecastDay) {
        String date = forecastDay.getString("date");
        JSONObject day = forecastDay.getJSONObject("day");

        double maxTempC = day.getDouble("maxtemp_c");
        double minTempC = day.getDouble("mintemp_c");
        String condition = day.getJSONObject("condition").getString("text");
        double avgWindSpeed = day.getDouble("maxwind_kph");
        double avgHumidity = day.getDouble("avghumidity");

        System.out.println("Date: " + date);
        System.out.println("Max Temp: " + maxTempC + "°C");
        System.out.println("Min Temp: " + minTempC + "°C");
        System.out.println("Condition: " + condition);
        System.out.println("Wind: " + avgWindSpeed + " km/h");
        System.out.println("Humidity: " + avgHumidity + "%");
        System.out.println("------------------------------------------------------------------------------");
    }
}