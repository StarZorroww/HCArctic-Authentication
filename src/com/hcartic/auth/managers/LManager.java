package com.hcartic.auth.managers;

import com.hcartic.auth.AuthMain;
import com.hcartic.auth.listeners.*;

import java.util.ArrayList;
import java.util.List;

public final class LManager {
    private AuthMain plugin;
    private List<AuthenticationListener> listeners;

    public LManager(AuthMain plugin, AManager aManager) {
        this.plugin = plugin;
        this.listeners = new ArrayList<>();
        this.listeners.add(new PlayerChatListener(plugin, aManager));
        this.listeners.add(new PlayerCommandListener(plugin, aManager));
        this.listeners.add(new PlayerDamageListener(plugin, aManager));
        this.listeners.add(new PlayerInteractListener(plugin, aManager));
        this.listeners.add(new PlayerJoinListener(plugin, aManager));
        this.listeners.add(new PlayerLeaveListener(plugin, aManager));
        this.listeners.add(new PlayerMoveListener(plugin, aManager));
    }

    public void registerListeners() {
        listeners.forEach(
                listener -> plugin.getServer().getPluginManager()
                .registerEvents(listener, plugin)
        );
    }
}
