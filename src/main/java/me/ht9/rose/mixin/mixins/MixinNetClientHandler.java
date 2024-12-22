package me.ht9.rose.mixin.mixins;

import me.ht9.rose.util.misc.GuiProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

@Mixin(NetClientHandler.class)
public class MixinNetClientHandler extends NetHandler
{
    @Shadow private NetworkManager netManager;

    @Inject(method = "<init>", at = @At(value = "NEW", target = "(Ljava/net/InetAddress;I)Ljava/net/Socket;"), cancellable = true)
    public void initSocket(Minecraft mc, String address, int port, CallbackInfo ci) throws IOException
    {
        ci.cancel();

        Socket socket;

        if (!GuiProxy.proxyIp.isEmpty())
        {
            String fullProxyIP = GuiProxy.proxyIp.trim();
            String[] split = fullProxyIP.split(":");

            if (split.length != 2) {
                this.netManager.networkShutdown("Invalid proxy!");
                return;
            }

            String proxyIp = split[0];
            int proxyPort;

            try
            {
                proxyPort = Integer.parseInt(split[1]);
            }
            catch (NumberFormatException e)
            {
                this.netManager.networkShutdown("Invalid proxy port! " + split[1]);
                return;
            }

            Proxy proxy = new Proxy(GuiProxy.type, new InetSocketAddress(proxyIp, proxyPort));
            socket = new Socket(proxy);
            socket.connect(new InetSocketAddress(address, port));
        }
        else
        {
            socket = new Socket(address, port);
        }

        this.netManager = new NetworkManager(socket, "Client", (NetClientHandler) (Object) this);
    }

    @Override
    public boolean isServerHandler() {
        return false;
    }
}
