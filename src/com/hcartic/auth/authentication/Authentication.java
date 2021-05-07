package com.hcartic.auth.authentication;

import com.hcartic.auth.managers.AManager;
import com.hcartic.auth.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public final class Authentication {
    private AManager aManager;

    private Player player;

    private final String AUTH_PREFIX;
    private final String SUCCESS_MESSAGE;
    public final String FAIL_MESSAGE;
    private final String AUTH_PASSED_ABOVE_THRESHOLD_MESSAGE;
    private final String FIRST_TIME_MESSAGE;

    private final int AUTH_LENGTH;
    private final int AUTH_THRESHOLD;

    private final boolean ENABLE_AUTH_THRESHOLD;
    private final boolean firstTime;

    private int attempts;

    public Authentication(AManager aManager, FileConfiguration config, Player player, boolean firstTime) {
        this.aManager = aManager;
        this.player = player;

        this.AUTH_PREFIX = Utilities.translate(config.getString("AUTH_PREFIX"));
        this.SUCCESS_MESSAGE = Utilities.translate(config.getString("SUCCESS_MESSAGE"));
        this.FAIL_MESSAGE = Utilities.translate(config.getString("FAIL_MESSAGE"));
        this.AUTH_PASSED_ABOVE_THRESHOLD_MESSAGE = Utilities.translate(config.getString("AUTH_PASSED_ABOVE_THRESHOLD_MESSAGE"));
        this.FIRST_TIME_MESSAGE = Utilities.translate(config.getString("FIRST_TIME_MESSAGE"));

        this.AUTH_LENGTH = config.getInt("auth-code-length");
        this.AUTH_THRESHOLD = config.getInt("incorrect-auth-threshold");

        this.ENABLE_AUTH_THRESHOLD = config.getBoolean("enable-incorrect-auth-threshold");
        this.firstTime = firstTime;

        this.attempts = 0;

        createAuthentication();
    }

    public void succeeded(String code) {
        if (attempts >= AUTH_THRESHOLD) {
            Bukkit.broadcast(Utilities.translate(AUTH_PASSED_ABOVE_THRESHOLD_MESSAGE
                    .replace("{prefix}", AUTH_PREFIX)
                    .replace("{player}", player.getName())
                    .replace("{attempts}", String.valueOf(attempts))
            ), "hcartic.auth.passed.attempts");
        }
        aManager.addAuthenticated(player, code);
        aManager.removeAuthentication(player);

        Utilities.sendMessage(player, SUCCESS_MESSAGE
                .replace("{prefix}", AUTH_PREFIX));
    }

    public void failed(String reason) {
        attempts++;

        Utilities.sendMessage(player, reason);
    }

    private void createAuthentication() {
        if (firstTime) {
            Utilities.sendMessage(player, FIRST_TIME_MESSAGE
                    .replace("{prefix}", AUTH_PREFIX)
                    .replace("{length}", String.valueOf(AUTH_LENGTH)));
            return;
        }
    }
}
