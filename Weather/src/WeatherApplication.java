import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApplication {
    // fetch weather data for given Location
    @SuppressWarnings("unchecked")
    public static JSONObject getWeatherData(String locationName) {
        // get location co-ordinates using the geo-location API(Application Programming
        // Interface)
        JSONArray locationData = getLocationData(locationName);

        // extract latitude and longitude
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // build API request URL with the location co-ordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=America%2FLos_Angeles";

        try {
            // call API and get Response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // check for response status
            // 200 means connection successful
            if (conn.getResponseCode() != 200) {
                System.out.println("ERROR: could't not connect to API");
                return null;

            }

            // store resulting Json data
            StringBuilder resultJSon = new StringBuilder();
            Scanner sc = new Scanner(conn.getInputStream());
            while (sc.hasNextLine()) {
                // read and store into the StringBuilder
                resultJSon.append(sc.nextLine());
            }

            // close the scanner
            sc.close();

            // close the connection
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJSon));

            // retrieve hourly date
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            // get the current hour's data
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            // get Temperature
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);


            // get wether code
            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            String wetherCondition = convertWeatherCode((long) weatherCode.get(index));

            // get humidity
            JSONArray relativehumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativehumidity.get(index);

            // get wind speed
            JSONArray relativewindSpeed = (JSONArray) hourly.get("wind_speed_10m");
            double windSpeed = (double) relativewindSpeed.get(index);

            // build the weather Json data object that we are going to access in our GUI
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", wetherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("wind_speed", windSpeed);

            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    // retrieve gegraphic co-ordinates from given location name
    public static JSONArray getLocationData(String locationName) {
        // replace any whitespace is location name
        locationName = locationName.replaceAll(" ", "+");

        // build API URL location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="
                + locationName + "&count=10&language=en&format=json";

        try {
            // call the API and get response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // check response status
            // 200 means connection successful
            if (conn.getResponseCode() != 200) {
                System.out.println("ERROR: could't connect to API");
                return null;

            } else {
                // store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner sc = new Scanner(conn.getInputStream());

                // read and store the resulting Json data into our StringBuilder
                while (sc.hasNext()) {
                    resultJson.append(sc.nextLine());

                }

                // close the scanner
                sc.close();

                // close url connection
                conn.disconnect();

                // parse the JSON string into JSON Obj
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                // get the list of location data the API generated from the location name
                JSONArray locationData = (JSONArray) resultJsonObj.get("results");
                return locationData;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Please Connect the Internet and Try Again!");

        }
        // could't find location
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            // attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to Get
            conn.setRequestMethod("GET");

            // connect to our API
            conn.connect();
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Please Connect the Internet and Try Again!");
        }
        // could't make connection
        return null;
        

        
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        // iterate through the time list and see which matches on current Time
        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                // return the Index
                return i;

            }
        }
        return 0;
    }

    public static String getCurrentTime() {
        // get current data and time
        LocalDateTime currenDateTime = LocalDateTime.now();

        // format date to be 2024-08-04T00:00 (this is how is read in the API)
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd" + " " + "'T'HH':00'");

        // format and print the current date and time
        String formattedDateTime = currenDateTime.format(dateTimeFormatter);

        return formattedDateTime;
    }

    private static String convertWeatherCode(long weatherCode) {
        // get the weather code data from the API
        String weatherCondition = "Unknown";
        if (weatherCode == 0L) {
            // Clear
            weatherCondition = "Clear";

        }
         else if (weatherCode <= 3L && weatherCode > 0L) {
            // Cloudy
            weatherCondition = "Cloudy";

        } 
        else if ((weatherCode >= 61L && weatherCode <= 67L)
            || (weatherCode >= 80L && weatherCode <= 86L)) {
                // Rain
            weatherCondition = "Rain";

        }else if (weatherCode <= 71L && weatherCode >= 77L) {
            // Snow
            weatherCondition = "Snow";

        } else if (weatherCode > 86L) {
            // ThunderStorm
            weatherCondition = "ThunderStorm";

        } 
        else if (weatherCode < 3L && weatherCode <= 48L) {
            // ThunderStorm
            weatherCondition = "Fogg";

        } 

        return weatherCondition;

    }
}
