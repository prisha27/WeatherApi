import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class App extends Application {
    private static final String API_KEY = "6eb7933611b64558b7e72119231411";
    private static final String API_URL = "http://api.weatherapi.com/v1/current.json";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather App");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(new Label("Enter city name:"));

        TextField cityInput = new TextField();
        Button searchButton = new Button("Enter");
        HBox searchBox = new HBox(10, cityInput, searchButton);
        searchBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(searchBox);

        VBox weatherInfoBox = new VBox(10);
        weatherInfoBox.setAlignment(Pos.CENTER);

        searchButton.setOnAction(event -> {
            String cityName = cityInput.getText();
            if (!cityName.isEmpty()) {
                JSONObject weatherData = getWeatherData(cityName);
                if (weatherData != null) {
                    JSONObject currentObject = (JSONObject) weatherData.get("current");
                    String description = (String) currentObject.get("condition").toString();
                    double temperature = Double.parseDouble(currentObject.get("temp_c").toString());
                    String weatherInfo = "Weather: " + description + "\nTemperature: " + temperature + "°C";
                    weatherInfoBox.getChildren().clear();
                    weatherInfoBox.getChildren().add(new Label(weatherInfo));
                } else {
                    weatherInfoBox.getChildren().clear();
                    weatherInfoBox.getChildren().add(new Label("City not found or weather data unavailable."));
                }
            }
        });

        borderPane.setBottom(weatherInfoBox);

        Scene scene = new Scene(borderPane, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private JSONObject getWeatherData(String city) {
        try {
            URI uri = new URI(API_URL + "?key=" + API_KEY + "&q=" + city);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                StringBuilder response = new StringBuilder();
                Scanner scanner = new Scanner(new InputStreamReader(conn.getInputStream()));
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                JSONParser parser = new JSONParser();
                return (JSONObject) parser.parse(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
