package com.hcartic.auth.listeners;

import com.hcartic.auth.AuthMain;
import com.hcartic.auth.managers.AManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener extends AuthenticationListener {
    public PlayerDamageListener(AuthMain plugin, AManager aManager) {
        super(plugin, aManager);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();

            if (aManager.playerDoingAuthentication(victim))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();

            if (aManager.playerDoingAuthentication(victim))
                event.setCancelled(true);
        }
    }
}
