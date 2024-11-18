uniform sampler2D texture;

uniform vec2 resolution;
uniform float time;

uniform float red;
uniform float green;
uniform float blue;

uniform bool rainbow;
uniform bool fill;
uniform bool dotted;

void main() {
    vec4 centerCol = texture2D(texture, gl_TexCoord[0].xy);

    if (centerCol.a == 0)
    {
        float alpha = 0;
        float texX = 1 / resolution.x;
        float texY = 1 / resolution.y;
        for (float x = -3; x < 3; x++)
        {
            for (float y = -3; y < 3; y++)
            {
                vec4 currentColor = texture2D(texture, gl_TexCoord[0].xy + vec2(texX * x, texY * y));
                if (currentColor.a != 0)
                {
                    alpha += max(0, (10 - distance(vec2(x, y), vec2(0))) / 100);
                }
            }
        }
        gl_FragColor = vec4(red, green, blue, alpha);
    }
    else
    {
        float alpha = 0.0;
        if (fill)
        {
            alpha = 0.15;
            if (dotted)
            {
                int x = int(gl_FragCoord.x);
                int y = int(gl_FragCoord.y);
                if (x % 15 == 0 && y % 15 == 0)
                {
                    alpha = 0.5;
                }
                x++;
                if (x % 15 == 0 && y % 15 == 0)
                {
                    alpha = 0.5;
                }
                x -= 2;
                if (x % 15 == 0 && y % 15 == 0)
                {
                    alpha = 0.5;
                }
                x++;
                y++;
                if (x % 15 == 0 && y % 15 == 0)
                {
                    alpha = 0.5;
                }
                y -= 2;
                if (x % 15 == 0 && y % 15 == 0)
                {
                    alpha = 0.5;
                }
            }
        }
        gl_FragColor = vec4(red, green, blue, alpha);
    }

    if (rainbow)
    {
        float x = gl_FragCoord.x;
        float y = gl_FragCoord.y;
        float tot = x + y;
        float dist = tot / 1000.0;
        float value = dist + (time / 3.0);
        value = mod(value, 1.0);

        float hue = value - 0.25;

        vec3 c = vec3(hue, 0.5, 1.0);
        vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
        vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
        vec3 rainbow = c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);

        gl_FragColor = vec4(rainbow.r, rainbow.g, rainbow.b, gl_FragColor.a);
    }
}
