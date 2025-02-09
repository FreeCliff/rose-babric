package me.ht9.rose.util.misc;

import me.howard.multiprotonostapi.protocol.ProtocolVersionManager;

public final class MultiprotoIntegrationHelper
{
    public static int getProtocolVersionAsInt()
    {
        return ProtocolVersionManager.version().version;
    }
}
