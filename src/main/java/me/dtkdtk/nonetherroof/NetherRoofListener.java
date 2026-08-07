package me.dtkdtk.nonetherroof;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.plugin.PluginContainer;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NetherRoofListener {

    private final Config config;
    private final PluginContainer container;
    private final Set<UUID> pendingTeleports = new HashSet<>();
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();

    public NetherRoofListener(Config config, PluginContainer container) {
        this.config = config;
        this.container = container;
    }

    @Listener(order = Order.EARLY)
    public void onPlayerMove(MoveEntityEvent event) {
        if (!(event.entity() instanceof ServerPlayer)) return;
        performCheck((ServerPlayer) event.entity());
    }

    @Listener
    public void onPlayerJoin(ServerSideConnectionEvent.Join event) {
        performCheck(event.player());
    }

    private void performCheck(ServerPlayer player) {
        if (player.hasPermission("no_nether_roof.bypass")) return;

        ServerWorld world = player.world();
        String dimensionId = world.key().asString();
        if (!dimensionId.equals("minecraft:the_nether")) return;

        double y = player.position().y();
        int roofY = config.getRoofY();
        if (y >= roofY) {
            if (pendingTeleports.contains(player.uniqueId())) {
                return;
            }
            scheduleTeleport(player, roofY);
        }
    }

    private void scheduleTeleport(ServerPlayer player, int roofY) {
        UUID uuid = player.uniqueId();
        if (pendingTeleports.contains(uuid)) return;
        pendingTeleports.add(uuid);

        Task task = Task.builder()
                .execute(() -> {
                    try {
                        if (player.isOnline()) {
                            double currentY = player.position().y();
                            if (currentY >= roofY) {
                                teleportBelowRoof(player, roofY);
                                tellPlayer(player);
                            }
                        }
                    } finally {
                        pendingTeleports.remove(uuid);
                    }
                })
                .plugin(container)
                .delay(Duration.ofMillis(1))
                .build();

        Sponge.server().scheduler().submit(task);
    }

    private void teleportBelowRoof(ServerPlayer player, int roofY) {
        ServerLocation loc = player.serverLocation();
        int teleportYOffset = 2;
        int newY = roofY - teleportYOffset;
        if (newY < 0) newY = 0;
        ServerLocation newLoc = ServerLocation.of(loc.world(), loc.x(), newY, loc.z());
        player.setLocation(newLoc);
    }

    private void tellPlayer(ServerPlayer player) {
        String msg = config.getDenyMessage();
        Component txt = legacySerializer.deserialize(msg);
        if (config.getUseActionBar()) {
            player.sendActionBar(txt);
        } else {
            player.sendMessage(txt);
        }
    }
}