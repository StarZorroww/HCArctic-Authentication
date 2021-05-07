package com.hcartic.auth.listeners;

import com.hcartic.auth.AuthMain;
import com.hcartic.auth.managers.AManager;
import com.hcartic.auth.utils.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public final class PlayerCommandListener extends AuthenticationListener {
    private final String BLOCK_COMMAND_MESSAGE;

    public PlayerCommandListener(AuthMain plugin, AManager aManager) {
        super(plugin, aManager);
        this.BLOCK_COMMAND_MESSAGE = Utilities.translate(plugin.getConfig()
        .getString("BLOCK_COMMAND_MESSAGE"));
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (aManager.playerDoingAuthentication(player)) {
            event.setCancelled(true);
            player.sendMessage(BLOCK_COMMAND_MESSAGE
            .replace("{prefix}", aManager.AUTH_PREFIX));
        }
    }
}
