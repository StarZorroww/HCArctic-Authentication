package com.hcartic.auth.listeners;

import com.hcartic.auth.AuthMain;
import com.hcartic.auth.managers.AManager;
import org.bukkit.event.Listener;


public class AuthenticationListener implements Listener {
    protected AuthMain plugin;
    protected AManager aManager;

    public AuthenticationListener(AuthMain plugin, AManager aManager) {
        this.plugin = plugin;
        this.aManager = aManager;
    }
}
