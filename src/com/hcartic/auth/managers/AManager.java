package com.hcartic.auth.managers;

import com.hcartic.auth.AuthMain;
import com.hcartic.auth.authentication.Authentication;
import com.hcartic.auth.database.MySQL;
import com.hcartic.auth.utils.AuthenticationUtilities;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.*;

public class AManager {

    private static AuthMain plugin;

    private FileConfiguration config;

    private int key = 0;
    private int hashedIP = 1;

    private static Map<UUID, List<String>> validAuth;
    private static Map<Player, Authentication> authentications;

    public static String AUTH_PREFIX;
    public static String SPECIAL_CHARACTER_MESSAGE;
    public static String AUTH_CHANGED_MESSAGE;
    public static String NO_AUTH_MESSAGE;
    public static String AUTH_CHANGED_INSTIGATOR_MESSAGE;
    public static String AUTH_CODE_LENGTH_MESSAGE;

    public static int AUTH_LENGTH;

    public AManager(AuthMain plugin, Map<UUID, List<String>> auth) {
        this.plugin = plugin;

        this.config = plugin.getConfig();


        validAuth = new HashMap<>();
        for (UUID playerUUID : auth.keySet()) {
            List<String> valueSet = auth.get(playerUUID);
            try {
                validAuth.put(playerUUID, valueSet);
            } catch (IllegalArgumentException i) {
                plugin.getLogger().severe( playerUUID + " isn't a valid uuid.");
            }
        }
        authentications = new HashMap<>();

        AUTH_PREFIX = plugin.getConfig().getString("AUTH_PREFIX");
        SPECIAL_CHARACTER_MESSAGE = plugin.getConfig().getString("SPECIAL_CHARACTER_MESSAGE");
        AUTH_CHANGED_MESSAGE = plugin.getConfig().getString("AUTH_CHANGED_MESSAGE");
        NO_AUTH_MESSAGE = plugin.getConfig().getString("NO_AUTH_MESSAGE");
        AUTH_CHANGED_INSTIGATOR_MESSAGE = plugin.getConfig().getString("AUTH_CHANGED_INSTIGATOR_MESSAGE");
        AUTH_CODE_LENGTH_MESSAGE = plugin.getConfig().getString("AUTH_CODE_LENGTH_MESSAGE");

        AUTH_LENGTH = config.getInt("auth-code-length");
    }

    public void createAuthentication(Player player) {
        authentications.put(player, new Authentication(this, config, player, false));
    }

    public void createFirstTimeAuthentication(Player player) {
        authentications.put(player, new Authentication(this, config, player, true));
    }

    public void removeAuthentication(Player player) {
        authentications.remove(player);
    }

    public static Authentication getPlayerAuthentication(Player player) {
        return authentications.get(player);
    }

    public boolean playerDoingAuthentication(Player player) {
        return authentications.containsKey(player);
    }

    public boolean playerHasNotAuthenticated(Player player) {
        return !validAuth.containsKey(player.getUniqueId());
    }

    public void addAuthenticated(Player player, String code) {
        // Change the players known IP in the documentation to the players new IP
        String ip = player.getAddress().getAddress().getHostAddress();
        List<String> documentation;
        if (!AuthenticationUtilities.hasDocumentation(player)) {
            documentation = new ArrayList<>();
            getPlayerAuthentication().put(player.getUniqueId(), documentation);
            documentation.add(key, code);
            documentation.add(hashedIP, AuthenticationUtilities.encodeString(ip));
        } else {
            documentation = validAuth.get(player.getUniqueId());
            documentation.set(key, code);
            documentation.set(hashedIP, AuthenticationUtilities.encodeString(ip));
        }

        // Add the new documentation to the validAuth list.
        validAuth.put(player.getUniqueId(), documentation);
    }

    public static void disable() {
        // Delete all rows because I hate and insert everything again because once again, I hate MySQL
        boolean empty = false;
        String query = "SELECT * FROM documentation LIMIT 1";
        ResultSet rs = MySQL.query(query);
        if (rs == null) {
            plugin.getLogger().info("[Authentication] Cannot delete data as nothing was found. (This is fine btw)");
            empty = true;
        }

        if (!empty) {
            query = "DELETE FROM documentation";
            MySQL.update(query);
        }

        if (getPlayerAuthentication() == null) {
            return;
        }

        for (UUID playerUUID : validAuth.keySet()) {
            String uuid = playerUUID.toString();
            String code = getDocumentation(playerUUID).get(0);
            String ip = getDocumentation(playerUUID).get(1);

            query = "INSERT INTO documentation (uuid, code, ip) " +
                    "VALUES ('" + uuid + "', '" + code + "', '" + ip + "')";

            MySQL.update(query);
        }
    }

    public static Map<UUID, List<String>> getPlayerAuthentication() {
        return validAuth;
    }

    public static List<String> getDocumentation(UUID uuid) {
        return validAuth.get(uuid);
    }

    public static AuthMain INSTANCE() {
        return plugin;
    }
}
