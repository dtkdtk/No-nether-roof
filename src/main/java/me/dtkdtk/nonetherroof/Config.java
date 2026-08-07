package me.dtkdtk.nonetherroof;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.nio.file.Path;

public class Config {

    private final Path configPath;
    private CommentedConfigurationNode root;

    private final int defaultRoofY = 127;
    private final String defaultDenyMessage = "&4You can't be above the roof of the Nether!";
    private final boolean defaultUseActionBar = true;

    private int roofY;
    private String denyMessage;
    private boolean useActionBar;

    public Config(Path configDir) {
        this.configPath = configDir.resolve("No_nether_roof.conf");
    }

    public void load() {
        try {
            ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().path(configPath).build();
            root = loader.load();
            if (!configPath.toFile().exists()) {
                setDefaults();
                loader.save(root);
            }
            roofY = root.node("roofY").getInt(defaultRoofY);
            denyMessage = root.node("denyMessage").getString(defaultDenyMessage);
            useActionBar = root.node("useActionBar").getBoolean(defaultUseActionBar);
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        try {
            root.node("roofY").set(Integer.class, defaultRoofY);
            root.node("denyMessage").set(String.class, defaultDenyMessage);
            root.node("useActionBar").set(Boolean.class, defaultUseActionBar);
        } catch (SerializationException e) {
            //never happens
        }
    }

    public int getRoofY() {
        return roofY;
    }

    public String getDenyMessage() {
        return denyMessage;
    }

    public boolean getUseActionBar() {
        return useActionBar;
    }
}