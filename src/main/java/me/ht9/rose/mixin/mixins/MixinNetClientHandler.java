package me.ht9.rose.mixin.mixins;

import me.ht9.rose.util.misc.GuiProxy;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

@Mixin(NetClientHandler.class)
public class MixinNetClientHandler
{
    @Shadow private NetworkManager netManager;

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/net/InetAddress;I)Ljava/net/Socket;"
            )
    )
    public Socket initSocket(InetAddress address, int port) throws IOException
    {
        if (GuiProxy.proxyIp.isEmpty())
        {
            return new Socket(address, port);
        }

        String fullProxyIP = GuiProxy.proxyIp.trim();
        String[] split = fullProxyIP.split(":");

        if (split.length != 2) {
            this.netManager.networkShutdown("Invalid proxy!");
            return null;
        }

        String proxyIp = split[0];
        int proxyPort;

        try
        {
            proxyPort = Integer.parseInt(split[1]);
        }
        catch (NumberFormatException e)
        {
            this.netManager.networkShutdown("Invalid port in proxy!");
            return null;
        }

        Proxy proxy = new Proxy(GuiProxy.type, new InetSocketAddress(proxyIp, proxyPort));
        Socket socket = new Socket(proxy);
        socket.connect(new InetSocketAddress(address, port));
        return socket;
    }
}
