package dev.stormwatch.cs4300;

import dev.stormwatch.cs4300.rendering.Shader;
import dev.stormwatch.cs4300.rendering.Shape;
import dev.stormwatch.cs4300.resource.Config;
import dev.stormwatch.cs4300.resource.ResourceManager;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Optional;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class CS4300 {

    private long window;

    public void run() {
        int windowWidth = 800;
        int windowHeight = 480;
        Optional<Config.TestConfig> testConfig = Config.loadTestConfig("testsettings.json");
        if (testConfig.isPresent()) {
            Config.TestConfig config = testConfig.get();
            windowWidth = config.windowWidth();
            windowHeight = config.windowHeight();
        }

        init(windowWidth, windowHeight);
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init(int windowWidth, int windowHeight) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        window = glfwCreateWindow(windowWidth, windowHeight, "Big Day", NULL, NULL);
        if ( window == NULL ) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Set up a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();
        glClearColor(0.0f, 0.1f, 0.1f, 1.0f);

        Shape.Triangle triangle = new Shape.Triangle();
        Optional<Shader> triangleShader = ResourceManager.getShader(triangle.getShaderName());

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            triangleShader.ifPresent((shader) -> {
                shader.use();
                glBindVertexArray(triangle.getVAO());
                glDrawArrays(GL_TRIANGLES, 0, 3);
            });

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        ResourceManager.disposeResources();
    }

    public static void main(String[] args) {
        new CS4300().run();
    }

}
