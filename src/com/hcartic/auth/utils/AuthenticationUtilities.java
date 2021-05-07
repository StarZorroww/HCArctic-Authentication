package com.hcartic.auth.utils;

import com.hcartic.auth.managers.AManager;
import org.bukkit.entity.Player;

import java.util.*;

public class AuthenticationUtilities {

    private static int key = 0;
    private static int hashedIP = 1;

    public static boolean isAlphaNumeric(char s) {
        return Character.isLetterOrDigit(s);
    }


    public static boolean hasDocumentation(Player player) {
        return AManager.getDocumentation(player.getUniqueId()) != null;
    }

    public static boolean hasAuthenticationCode(Player player) {
        return AManager.getDocumentation(player.getUniqueId()).get(0) != null;
    }

    public static boolean isAuthenticationCode(Player player, String code) {
        return AManager.getDocumentation(player.getUniqueId()).get(0).equals(code);
    }

    public static void changePlayerAuthenticationCode(Player instigator, Player player, String code) {
        // Check if the player has any documentation
        if (!hasDocumentation(player)) {
            Utilities.sendMessage(instigator, AManager.NO_AUTH_MESSAGE
            .replace("{prefix}", AManager.AUTH_PREFIX));
            return;
        }

        // Check the new code for special characters
        for (int i = 0; i < code.length(); i++) {
            if (!isAlphaNumeric(code.charAt(i))) {
                Utilities.sendMessage(instigator, AManager.SPECIAL_CHARACTER_MESSAGE
                .replace("{prefix}", AManager.AUTH_PREFIX));
                return;
            }
        }

        UUID uuid = player.getUniqueId();

        // Get the players documentation so we can change their authentication code
        List<String> documentation = AManager.getDocumentation(player.getUniqueId());
        documentation.set(key, code);

        // Change the players documentation to the new one
        AManager.getPlayerAuthentication().put(uuid, documentation);

        Utilities.sendMessage(instigator, AManager.AUTH_CHANGED_INSTIGATOR_MESSAGE
        .replace("{prefix}", AManager.AUTH_PREFIX)
        .replace("{player}", player.getName()));
    }

    public static void addPlayerIPAndAuthenticationCode(Player player, String code) {
        if (!hasDocumentation(player)) {
            List<String> documentation = new ArrayList<>();

            documentation.set(key, code);
            documentation.set(hashedIP, encodeString(player.getAddress().getAddress().getHostAddress()));

            AManager.getPlayerAuthentication().put(player.getUniqueId(),
                    documentation);
            return;
        }

        List<String> documentation = AManager.getDocumentation(player.getUniqueId());

        documentation.set(hashedIP, encodeString(player.getAddress().getAddress().getHostAddress()));

        if (hasIPChanged(player)) {
            AManager.getPlayerAuthentication().put(player.getUniqueId(), documentation);
        }
    }

    public static void authenticationAction(Player player, String message) {
        if (hasDocumentation(player) && hasAuthenticationCode(player)) {
            if (message.length() < AManager.AUTH_LENGTH) {
                AManager.getPlayerAuthentication(player).failed(AManager.AUTH_CODE_LENGTH_MESSAGE
                        .replace("{prefix}", AManager.AUTH_PREFIX)
                        .replace("{length}", String.valueOf(AManager.AUTH_LENGTH)));
                return;
            }
            for (int i = 0; i < message.length(); i++) {
                if (!isAlphaNumeric(message.charAt(i))) {
                    AManager.getPlayerAuthentication(player).failed(AManager.SPECIAL_CHARACTER_MESSAGE
                            .replace("{prefix}", AManager.AUTH_PREFIX));
                    return;
                }
            }
            if (!isAuthenticationCode(player, message)) {
                AManager.getPlayerAuthentication(player)
                        .failed(AManager.getPlayerAuthentication(player).FAIL_MESSAGE
                                .replace("{prefix}", AManager.AUTH_PREFIX));
                return;
            }
            AManager.getPlayerAuthentication(player)
                    .succeeded(message);
            addPlayerIPAndAuthenticationCode(player, message);
            return;
        }
        if (message.length() < AManager.AUTH_LENGTH) {
            AManager.getPlayerAuthentication(player).failed(AManager.AUTH_CODE_LENGTH_MESSAGE
                    .replace("{prefix}", AManager.AUTH_PREFIX)
                    .replace("{length}", String.valueOf(AManager.AUTH_LENGTH)));
            return;
        }
        for (int i = 0; i < message.length(); i++) {
            if (!isAlphaNumeric(message.charAt(i))) {
                AManager.getPlayerAuthentication(player).failed(AManager.SPECIAL_CHARACTER_MESSAGE
                        .replace("{prefix}", AManager.AUTH_PREFIX));
                return;
            }
        }

        AManager.getPlayerAuthentication(player)
                .succeeded(message);
        addPlayerIPAndAuthenticationCode(player, message);
    }

    public static boolean hasIPChanged(Player player) {
        // Check if the player has any documentation
        if (!hasDocumentation(player)) return false;

        // Get the players documentation so we can compare the players known IP with their current IP
        List<String> documentation = AManager.getDocumentation(player.getUniqueId());
        String knownIP = documentation.get(1);

        // Get the players current IP and hash it
        String ip = player.getAddress().getAddress().getHostAddress();
        String encodedIP = encodeString(ip);

        // Compare the 2 strings
        return !knownIP.equals(encodedIP);
    }

    public static String encodeString(String s) {
        return Base64.getEncoder().encodeToString(s.getBytes());
    }

    public static String decodeString(String s) {
        return Arrays.toString(Base64.getDecoder().decode(s.getBytes()));
    }

}