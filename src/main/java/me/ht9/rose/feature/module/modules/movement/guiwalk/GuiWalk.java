package me.ht9.rose.feature.module.modules.movement.guiwalk;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.MoveStateUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiEditSign;
import org.lwjgl.input.Keyboard;

@Description("Lets you walk while in guis")
public final class GuiWalk extends Module
{
    private static final GuiWalk instance = new GuiWalk();

    private final Setting<Boolean> shift = new Setting<>("Shift", false);

    @SubscribeEvent(priority = 5)
    public void onUpdate(MoveStateUpdateEvent event)
    {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditSign))
        {
            event.setMoveForward(Keyboard.isKeyDown(mc.gameSettings.keyBindForward.keyCode));
            event.setMoveBack(Keyboard.isKeyDown(mc.gameSettings.keyBindBack.keyCode));
            event.setMoveLeft(Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.keyCode));
            event.setMoveRight(Keyboard.isKeyDown(mc.gameSettings.keyBindRight.keyCode));
            event.setJump(Keyboard.isKeyDown(mc.gameSettings.keyBindJump.keyCode));

            if (shift.value())
                event.setSneak(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode));

            event.setDontReset(true);
        }
    }

    public static GuiWalk instance()
    {
        return instance;
    }
}
