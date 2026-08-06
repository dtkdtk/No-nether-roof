package me.dtkdtk.nonetherroof;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

public class Config {

    private final Path configPath;
    private CommentedConfigurationNode root;

    private final int defaultRoofY = 127;
    private final String defaultDenyMessage = "&4You can't be above the roof of Nether!";
    private final boolean defaultUseActionBar = true;

    private int roofY;
    private String denyMessage;
    private boolean useActionBar;

    public Config(Path configDir) {
        this.configPath = configDir.resolve("No_nether_roof.conf");
    }

    public void load() {
        try {
            ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setPath(configPath).build();
            root = loader.load();
            if (!configPath.toFile().exists()) {
                setDefaults();
                loader.save(root);
            }
            roofY = root.getNode("roofY").getInt(defaultRoofY);
            denyMessage = root.getNode("denyMessage").getString(defaultDenyMessage);
            useActionBar = root.getNode("useActionBar").getBoolean(defaultUseActionBar);
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        root.getNode("roofY").setValue(defaultRoofY);
        root.getNode("denyMessage").setValue(defaultDenyMessage);
        root.getNode("useActionBar").setValue(defaultUseActionBar);
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