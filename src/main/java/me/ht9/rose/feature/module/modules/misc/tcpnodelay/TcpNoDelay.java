package me.ht9.rose.feature.module.modules.misc.tcpnodelay;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

@Description("Rejoin required for this to take effect")
public final class TcpNoDelay extends Module
{
    private static final TcpNoDelay instance = new TcpNoDelay();

    public static TcpNoDelay instance()
    {
        return instance;
    }
}
