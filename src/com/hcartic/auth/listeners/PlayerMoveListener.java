package com.hcartic.auth.listeners;

import com.hcartic.auth.AuthMain;
import com.hcartic.auth.managers.AManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener extends AuthenticationListener {
    public PlayerMoveListener(AuthMain plugin, AManager aManager) {
        super(plugin, aManager);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (aManager.playerDoingAuthentication(player)) {
            if (event.getFrom().getX() != event.getTo().getX()
                    || event.getFrom().getZ() != event.getTo().getZ())
            player.teleport(event.getFrom().setDirection(event.getTo().getDirection()));
        }
    }
}
