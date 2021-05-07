package com.hcartic.auth.listeners;

import com.hcartic.auth.AuthMain;
import com.hcartic.auth.managers.AManager;
import com.hcartic.auth.utils.AuthenticationUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener extends AuthenticationListener {
    public PlayerJoinListener(AuthMain plugin, AManager aManager) {
        super(plugin, aManager);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("hcartic.auth")) {
            if (AuthenticationUtilities.hasDocumentation(player)
                    && AuthenticationUtilities.hasIPChanged(player)) {
                if (AuthenticationUtilities.hasAuthenticationCode(player))
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                            () -> aManager.createAuthentication(player), 5);
                return;
            } else if (!AuthenticationUtilities.hasDocumentation(player))
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                        () -> aManager.createFirstTimeAuthentication(player), 5);
        }
    }
}