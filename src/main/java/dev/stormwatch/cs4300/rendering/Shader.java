package dev.stormwatch.cs4300.rendering;

import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {

    private final int shaderProgram;

    public Shader(int shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public static Optional<Shader> createShaderProgram(String vertexSource, String fragmentSource) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            int logLength = glGetShaderi(vertexShader, GL_INFO_LOG_LENGTH);
            System.err.println("Failed to compile vertex shader: " + glGetShaderInfoLog(vertexShader, logLength));
            return Optional.empty();
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            int logLength = glGetShaderi(fragmentShader, GL_INFO_LOG_LENGTH);
            System.err.println("Failed to compile fragment shader: " + glGetShaderInfoLog(fragmentShader, logLength));
            return Optional.empty();
        }

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            int logLength = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("Failed to link shader program: " + glGetProgramInfoLog(shaderProgram, logLength));
            return Optional.empty();
        }

        return Optional.of(new Shader(shaderProgram));
    }

    public int getShaderProgram() {
        return this.shaderProgram;
    }

    public void use() {
        glUseProgram(this.shaderProgram);
    }

}
