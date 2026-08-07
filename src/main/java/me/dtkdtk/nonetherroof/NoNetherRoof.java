package me.dtkdtk.nonetherroof;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.LoadedGameEvent;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.plugin.PluginContainer;

import java.nio.file.Path;

public class NoNetherRoof {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    private Config config;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;

    @Listener
    public void onServerStart(LoadedGameEvent event) {
        config = new Config(configDir);
        config.load();
        logger.info("No Nether Roof plugin loaded");

        Sponge.eventManager().registerListeners(container, new NetherRoofListener(config, container));
    }

    @Listener
    public void onReload(RefreshGameEvent event) {
        if (config == null) return;
        config.load();
        logger.info("No Nether Roof plugin configuration reloaded");
    }
}