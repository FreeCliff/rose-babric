package me.ht9.rose.mixin.mixins;

import me.ht9.rose.feature.gui.GuiProxy;
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
import java.net.*;

@Mixin(NetClientHandler.class)
public class MixinNetClientHandler
{
    @Shadow private NetworkManager netManager;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NetworkManager;<init>(Ljava/net/Socket;Ljava/lang/String;Lnet/minecraft/src/NetHandler;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void initTcpNoDelay(Minecraft string, String i, int par3, CallbackInfo ci, Socket socket)
    {
        if (TcpNoDelay.instance().enabled())
        {
            try
            {
                socket.setTcpNoDelay(true);
            } catch (SocketException ignored) {}
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
