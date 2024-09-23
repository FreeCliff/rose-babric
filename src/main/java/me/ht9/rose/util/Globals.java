package me.ht9.rose.util;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;

public interface Globals
{
    Minecraft mc = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
}