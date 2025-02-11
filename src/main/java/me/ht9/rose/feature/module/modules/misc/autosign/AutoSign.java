package me.ht9.rose.feature.module.modules.misc.autosign;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.SignEditEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

@SuppressWarnings("unused")
@Description("Automatically writes to signs.")
public final class AutoSign extends Module
{
    private static final AutoSign instance = new AutoSign();

    private String[] lines;

    @Override
    public void onEnable()
    {
        this.lines = null;
    }

    @Override
    public void onDisable()
    {
        this.lines = null;
    }

    @SubscribeEvent
    public void onSignEdit(SignEditEvent event)
    {
        if (event.type() == SignEditEvent.Type.START)
        {
            if (this.lines != null)
            {
                event.setLine1(this.lines[0]);
                event.setLine2(this.lines[1]);
                event.setLine3(this.lines[2]);
                event.setLine4(this.lines[3]);
                event.setCancelled(true);
            }
        } else if (event.type() == SignEditEvent.Type.FINISH)
        {
            if (this.lines == null)
            {
                this.lines = new String[]{ event.line1(), event.line2(), event.line3(), event.line4() };
            }
        }
    }

    public static AutoSign instance()
    {
        return instance;
    }
}
