package me.ht9.rose.util.misc;

import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;

public class MultiprotoIntegrationHelper
{
    public static int getProtocolVersionAsInt()
    {
        return ProtocolVersionManager.getVersion().version;
    }
}
