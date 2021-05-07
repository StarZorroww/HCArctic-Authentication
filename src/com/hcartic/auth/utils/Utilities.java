package com.hcartic.auth.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class Utilities {

    public static String translate(String m) { return ChatColor.translateAlternateColorCodes('&', m); }

    public static void sendMessage(Player p, String m) { p.sendMessage(translate(m)); }
}
