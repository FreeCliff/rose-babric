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
                event.sign().signText = this.lines;
                event.setCancelled(true);
            }
        } else if (event.type() == SignEditEvent.Type.FINISH)
        {
            if (this.lines == null)
            {
                this.lines = event.sign().signText;
            }
        }
    }

    public static AutoSign instance()
    {
        return instance;
    }
}
