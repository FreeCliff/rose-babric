package me.ht9.rose.mixin.mixins;

import me.ht9.rose.feature.gui.proxy.GuiProxies;
import me.ht9.rose.feature.gui.proxy.ProxyData;
import me.ht9.rose.feature.module.modules.misc.tcpnodelay.TcpNoDelay;
import net.minecraft.client.Minecraft;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Mixin(NetClientHandler.class)
public class MixinNetClientHandler
{
    @Shadow
    private NetworkManager netManager;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NetworkManager;<init>(Ljava/net/Socket;Ljava/lang/String;Lnet/minecraft/src/NetHandler;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void initTcpNoDelay(Minecraft string, String i, int par3, CallbackInfo ci, Socket socket)
    {
        if (TcpNoDelay.instance().enabled())
        {
            try
            {
                socket.setTcpNoDelay(true);
            } catch (SocketException ignored)
            {
            }
        }
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/net/InetAddress;I)Ljava/net/Socket;"
            )
    )
    public Socket initSocket(InetAddress address, int port) throws IOException
    {
        ProxyData proxyData = GuiProxies.currentProxy();

        if (proxyData.ip() == null)
        {
            return new Socket(address, port);
        }

        String fullProxyIP = proxyData.ip().trim();
        String[] split = fullProxyIP.split(":");

        if (split.length != 2)
        {
            this.netManager.networkShutdown("Invalid proxy!");
            return null;
        }

        String proxyIp = split[0];
        int proxyPort;

        try
        {
            proxyPort = Integer.parseInt(split[1]);
        } catch (NumberFormatException e)
        {
            this.netManager.networkShutdown("Invalid port in proxy!");
            return null;
        }

        String auth = proxyData.auth().trim();
        String user = null;
        String pass = null;

        if (!auth.isEmpty())
        {
            String[] creds = auth.split(":", 2);

            if (creds.length != 2)
            {
                this.netManager.networkShutdown("Invalid proxy auth!");
                return null;
            }

            user = creds[0];
            pass = creds[1];
        }

        // thanks claude
        // still have no idea whether this works, I don't use HTTP proxies that require auth
        if (proxyData.type() == Proxy.Type.HTTP)
        {
            Socket socket = new Socket(proxyIp, proxyPort);

            try
            {
                String host = address.getHostAddress();

                if (host.indexOf(':') >= 0)
                {
                    host = "[" + host + "]"; // IPv6 literal
                }

                String hostPort = host + ":" + port;

                StringBuilder req = new StringBuilder();
                req.append("CONNECT ").append(hostPort).append(" HTTP/1.1\r\n");
                req.append("Host: ").append(hostPort).append("\r\n");

                if (user != null)
                {
                    String token = Base64.getEncoder().encodeToString(
                            (user + ":" + pass).getBytes(StandardCharsets.UTF_8));
                    req.append("Proxy-Authorization: Basic ").append(token).append("\r\n");
                }

                req.append("Proxy-Connection: Keep-Alive\r\n\n");

                OutputStream out = socket.getOutputStream();
                out.write(req.toString().getBytes(StandardCharsets.ISO_8859_1));
                out.flush();

                // Read the response headers byte by byte so we never swallow game data
                InputStream in = socket.getInputStream();
                StringBuilder resp = new StringBuilder();
                int b;

                while ((b = in.read()) != -1)
                {
                    resp.append((char) b);

                    int len = resp.length();

                    if (len >= 4 && resp.charAt(len - 4) == '\r' && resp.charAt(len - 3) == '\n' && resp.charAt(len - 2) == '\r' && resp.charAt(len - 1) == '\n')
                        break;

                    if (len > 16384)
                        throw new IOException("Proxy response too large");
                }

                String statusLine = resp.toString().split("\r\n", 2)[0];
                String[] parts = statusLine.split(" ", 3);

                if (parts.length < 2 || !parts[1].equals("200"))
                    throw new IOException("Proxy refused CONNECT: " + resp.toString().replace("\r\n", " | "));

                return socket;
            } catch (IOException e)
            {
                socket.close();
                throw e;
            }
        }

        if (user != null)
        {
            final String u = user;
            final char[] p = pass.toCharArray();

            Authenticator.setDefault(new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    if (proxyIp.equalsIgnoreCase(getRequestingHost()) && getRequestingPort() == proxyPort)
                        return new PasswordAuthentication(u, p);
                    return super.getPasswordAuthentication();
                }
            });
        } else Authenticator.setDefault(null);

        Proxy proxy = new Proxy(proxyData.type(), new InetSocketAddress(proxyIp, proxyPort));
        Socket socket = new Socket(proxy);
        socket.connect(new InetSocketAddress(address, port));
        return socket;
    }
}
