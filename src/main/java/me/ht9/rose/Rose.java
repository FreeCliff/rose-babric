package me.ht9.rose;

import me.ht9.rose.event.bus.EventBus;
import me.ht9.rose.event.factory.Factory;
import me.ht9.rose.feature.command.impl.Prediction;
import me.ht9.rose.feature.gui.clickgui.RoseGui;
import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.util.Globals;
import me.ht9.rose.util.config.FileUtils;
import me.ht9.rose.util.misc.ModMenuIntegrationHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Rose implements ClientModInitializer, Globals
{
	private static final String version = FabricLoader.getInstance().getModContainer("rose")
			.map(container -> container.getMetadata().getVersion().getFriendlyString())
			.orElse("unknown");

	private static final Logger logger = LogManager.getLogger("rose");

	private static final EventBus bus = new EventBus();

	private static final ExecutorService asyncExecutor = Executors.newSingleThreadExecutor();

	private static long startTime;
	
	@Override
	public void onInitializeClient()
	{
		startTime = System.nanoTime();
		logger.info("loading rose...");

		bus.register(Factory.instance());
		bus.register(Prediction.instance());

		Registry.loadModules();
		Registry.loadCommands();
		Registry.finishLoad();

		FileUtils.loadModules();
		FileUtils.loadClickGUI();
		FileUtils.loadFriends();

		if (MixinPlugin.isModMenuLoaded())
			ModMenuIntegrationHelper.addLegacyConfigScreenTask("rose", () -> RoseGui.instance().openGuiInMenu());

		Runtime.getRuntime().addShutdownHook(new Thread(() ->
		{
			FileUtils.saveModules();
			FileUtils.saveClickGUI();
			FileUtils.saveFriends();
		}));

		double elapsedTime = (System.nanoTime() - startTime) / 1000000000.0;
		logger.info("successfully loaded rose in {}s.", elapsedTime);
	}

	public static ExecutorService asyncExecutor()
	{
		return asyncExecutor;
	}

	public static EventBus bus()
	{
		return bus;
	}

	public static Logger logger()
	{
		return logger;
	}

	public static long startTime()
	{
		return startTime;
	}

	public static String version()
	{
		return version;
	}
}