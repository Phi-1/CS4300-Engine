package dev.stormwatch.cs4300.resource;

import dev.stormwatch.cs4300.rendering.Shader;
import dev.stormwatch.cs4300.util.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.lwjgl.opengl.GL20.*;

public class ResourceManager {

    private static final Map<String, Shader> SHADERS = new HashMap<>();

    public static Result storeShader(String name, Shader shader) {
        if (SHADERS.containsKey(name)) {
            return Result.error("Shader with name '" + name + "' already exists");
        }
        SHADERS.put(name, shader);
        return Result.success();
    }

    public static Optional<Shader> getShader(String name) {
        if (!SHADERS.containsKey(name)) {
            return Optional.empty();
        }
        return Optional.of(SHADERS.get(name));
    }

    public static void disposeResources() {
        // TODO: add new stuff to dispose of as I create them
        for (Shader shader : SHADERS.values()) {
            glDeleteShader(shader.getShaderProgram());
        }
    }

}
