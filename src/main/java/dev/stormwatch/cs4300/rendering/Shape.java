package dev.stormwatch.cs4300.rendering;

import dev.stormwatch.cs4300.rendering.Shader;
import dev.stormwatch.cs4300.resource.FileUtil;
import dev.stormwatch.cs4300.resource.ResourceManager;
import dev.stormwatch.cs4300.util.Result;

import java.util.Optional;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Shape {
    public static class Triangle {

        private static final String SHADER_NAME = "default";

        private final float[] vertices = {
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
                 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f,
                 0.0f,  0.5f, 0.0f, 0.0f, 0.0f, 1.0f
        };
        private int VAO;

        public Triangle() {
            // TODO: move to functions
            int VBO = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

            VAO = glGenVertexArrays();
            glBindVertexArray(VAO);

            glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            glBindVertexArray(0);

            // TODO: check if shader already stored
            Optional<String> vertexSource = FileUtil.getResource("shaders/default.vs");
            Optional<String> fragmentSource = FileUtil.getResource("shaders/default.fs");
            if (vertexSource.isEmpty() || fragmentSource.isEmpty()) {
                throw new RuntimeException("Failed to load shader file(s) for Triangle Shape");
            }
            Optional<Shader> shader = Shader.createShaderProgram(vertexSource.get(), fragmentSource.get());
            if (shader.isEmpty()) {
                throw new RuntimeException("Failed to create shader program for Triangle Shape");
            }

            ResourceManager.storeShader(SHADER_NAME, shader.get());
        }

        public int getVAO() {
            return this.VAO;
        }

        public String getShaderName() {
            return SHADER_NAME;
        }

    }

}
