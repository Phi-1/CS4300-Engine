package dev.stormwatch.cs4300.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class FileUtil {

    public static Optional<String> getResource(String filepath) {
        InputStream resource = FileUtil.class.getClassLoader().getResourceAsStream(filepath);
        if (resource == null) return Optional.empty();
        try {
            return Optional.of(new String(resource.readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Failed to load resource " + filepath + ": " + e);
            return Optional.empty();
        }
    }

}
