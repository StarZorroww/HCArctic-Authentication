package com.hcartic.auth.listeners;

import com.hcartic.auth.AuthMain;
import com.hcartic.auth.managers.AManager;
import com.hcartic.auth.utils.AuthenticationUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener extends AuthenticationListener {
    public PlayerChatListener(AuthMain plugin, AManager aManager) {
        super(plugin, aManager);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (aManager.playerDoingAuthentication(player)) {
            String message = event.getMessage();
            AuthenticationUtilities.authenticationAction(player, message);
            event.setCancelled(true);
        }
    }
}
