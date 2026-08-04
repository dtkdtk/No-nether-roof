package me.dtkdtk.nonetherroof;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;

@Plugin(
        id = "no_nether_roof",
        name = "NoNetherRoof",
        version = "1.0"
)
public class NoNetherRoof {

    @Inject
    private Logger logger;

    private Config config;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        config = new Config(configDir);
        config.load();
        logger.info("NoNetherRoof plugin loaded");

        Sponge.getEventManager().registerListeners(this, new NetherRoofListener(config, this));
    }

    @Listener
    public void onReload(GameReloadEvent e) {
        if (config == null) return;
        config.load();
        logger.info("NoNetherRoof plugin configuration reloaded");
    }
}