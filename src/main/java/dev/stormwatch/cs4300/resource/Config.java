package dev.stormwatch.cs4300.resource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class Config {

    private static final String CONFIG_PATH = "config/";

    public static Optional<TestConfig> loadTestConfig(String filename) {
        int windowWidth, windowHeight;

        Optional<String> testConfigFile = FileUtil.getResource(CONFIG_PATH + filename);
        if (testConfigFile.isEmpty()) return Optional.empty();
        try {
            JSONObject json = new JSONObject(testConfigFile.get());
            JSONObject windowSettings = (JSONObject) json.get("window");
            windowWidth = windowSettings.getInt("width");
            windowHeight = windowSettings.getInt("height");

            return Optional.of(new TestConfig(windowWidth, windowHeight));
        } catch (JSONException e) {
            System.err.println("Failed to parse json file " + filename + ": " + e);
            return Optional.empty();
        }
    }

    public record TestConfig(int windowWidth, int windowHeight) {}

}
