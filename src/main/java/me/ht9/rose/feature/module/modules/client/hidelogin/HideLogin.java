package me.ht9.rose.feature.module.modules.client.hidelogin;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.ChatGuiRenderEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;

@Description("Hide your AuthMe/xAuth login from chat")
public final class HideLogin extends Module
{
    private static final HideLogin instance = new HideLogin();

    private final Setting<Mode> mode = new Setting<>("Mode", Mode.Censor);

    @SubscribeEvent
    public void onChatGuiRender(ChatGuiRenderEvent event)
    {
        String text = event.text().toLowerCase();
        if ((text.startsWith("/login") || text.startsWith("/register") || text.startsWith("/changep")) && event.text().contains(" "))
        {
            int index = event.text().indexOf(" ") + 1;
            if (mode.value().equals(Mode.Censor))
            {
                event.setText(event.text().substring(0, index) + "*".repeat(event.text().length() - index));
            }
            else if (mode.value().equals(Mode.Hide))
            {
                event.setText(event.text().substring(0, index));
            }
        }
    }

    public static HideLogin instance()
    {
        return instance;
    }

    public enum Mode
    {
        Censor,
        Hide
    }
}
