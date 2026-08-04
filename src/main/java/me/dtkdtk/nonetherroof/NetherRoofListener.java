package me.dtkdtk.nonetherroof;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NetherRoofListener {

    private final Config config;
    private final NoNetherRoof plugin;
    private final Set<UUID> pendingTeleports = new HashSet<>();

    public NetherRoofListener(Config config, NoNetherRoof plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    @Listener(order = Order.EARLY)
    public void onPlayerMove(MoveEntityEvent event) {
        if (!(event.getTargetEntity() instanceof Player)) return;
        performCheck((Player) event.getTargetEntity());
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        performCheck(event.getTargetEntity());
    }

    private void performCheck(Player player) {
        if (player.hasPermission("netherroof.bypass")) return;

        World world = player.getWorld();
        if (!world.getDimension().getType().equals(DimensionTypes.NETHER)) return;

        double y = player.getLocation().getY();
        int roofY = config.getRoofY();
        if (y >= roofY) {
            if (pendingTeleports.contains(player.getUniqueId())) {
                return;
            }
            scheduleTeleport(player, roofY);
        }
    }

    private void scheduleTeleport(Player player, int roofY) {
        UUID uuid = player.getUniqueId();
        if (pendingTeleports.contains(uuid)) return;
        pendingTeleports.add(uuid);

        Task.builder()
                .execute(() -> {
                    try {
                        if (player.isOnline()) {
                            double currentY = player.getLocation().getY();
                            if (currentY >= roofY) {
                                teleportBelowRoof(player, roofY);
                                tellPlayer(player);
                            }
                        }
                    } finally {
                        pendingTeleports.remove(uuid);
                    }
                })
                .delay(1, TimeUnit.MILLISECONDS)
                .submit(plugin);
    }

    private void teleportBelowRoof(Player player, int roofY) {
        Location<World> loc = player.getLocation();
        int teleportYOffset = 2;
        int newY = roofY - teleportYOffset;
        if (newY < 0) newY = 0;
        Location<World> newLoc = new Location<>(loc.getExtent(), loc.getX(), newY, loc.getZ());
        player.setLocation(newLoc);
    }

    private void tellPlayer(Player player) {
        String msg = config.getDenyMessage();
        Text txt = TextSerializers.FORMATTING_CODE.deserialize(msg);
        if (config.getUseActionBar()) {
            player.sendTitle(Title.builder().actionBar(txt).build());
        } else {
            player.sendMessage(txt);
        }
    }
}