package me.ht9.rose.util.render.shader;

import me.ht9.rose.Rose;
import me.ht9.rose.util.Globals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements Globals
{
    private final int programID;
    private final long initTime = System.nanoTime();

    public ShaderProgram(String vertex, String fragment)
    {
        int vertexID = loadShader(Rose.class.getResourceAsStream(vertex), GL_VERTEX_SHADER);
        int fragmentID = loadShader(Rose.class.getResourceAsStream(fragment), GL_FRAGMENT_SHADER);

        programID = glCreateProgram();
        glAttachShader(programID, vertexID);
        glAttachShader(programID, fragmentID);
        glLinkProgram(programID);
        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
    }

    public void useShader()
    {
        glUseProgram(programID);
    }

    public void stopShader()
    {
        glUseProgram(0);
    }

    private static int loadShader(InputStream file, int type)
    {
        StringBuilder shaderSource = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String line;
            while ((line = reader.readLine()) != null)
            {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e)
        {
            Rose.logger().error("Failed to load shader", e);
        }

        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE)
        {
            Rose.logger().error("Could not compile shader!\n{}", glGetShaderInfoLog(shaderID, 500));
        }
        return shaderID;
    }

    public int getUniform(String name)
    {
        return glGetUniformLocation(programID, name);
    }
}
