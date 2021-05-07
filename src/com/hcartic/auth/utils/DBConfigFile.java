package com.hcartic.auth.utils;

import com.hcartic.auth.AuthMain;
import com.hcartic.auth.managers.AManager;

public class DBConfigFile {

    private static final ConfigFile dataBase = AuthMain.getDataBaseConfig();

    private static final String host = "host";
    private static final String user = "user";
    private static final String password = "password";
    private static final String database = "database";
    private static final String port = "port";
    private static final String ssl = "use_SSL";

    public static void clear() {
        set("host", "", false);
        set("user", "", false);
        set("password", "", false);
        set("database", "", false);
        set("port", "3306", false);
        set("use_SSL", true, false);
    }

    public static void create() {
        set("host", "", true);
        set("user", "", true);
        set("password", "", true);
        set("database", "", true);
        set("port", "3306", true);
        set("use_SSL", true, true);
    }

    public static void reload() {
        dataBase.reload();
        create();
    }

    public static void setHost(final String s) {
        if (!getHost().equalsIgnoreCase(s)) {
            set("host", s, false);
        }
    }

    public static void setUser(final String s) {
        if (!getUser().equalsIgnoreCase(s)) {
            set("user", s, false);
        }
    }

    public static void setPassword(final String s) {
        if (!getPassword().equalsIgnoreCase(s)) {
            set("password", s, false);
        }
    }

    public static void setDatabase(final String s) {
        if (!getDatabase().equalsIgnoreCase(s)) {
            set("database", s, false);
        }
    }

    public static void setPort(final String s) {
        if (!getPort().equalsIgnoreCase(s)) {
            set("port", s, false);
        }
    }

    public static void setSSL(final boolean b) {
        if (getSSL() != b) {
            set("use_SSL", b, false);
        }
    }

    public static String getHost() {
        return get("host");
    }

    public static String getUser() {
        return get("user");
    }

    public static String getPassword() {
        return get("password");
    }

    public static String getDatabase() {
        return get("database");
    }

    public static String getPort() {
        return get("port");
    }

    public static boolean getSSL() {
        return getBoolean("use_SSL");
    }

    private static void set(final String name, final Object value, final boolean checkIfExists) {
        if (name == null || value == null || (checkIfExists && dataBase.getConfig().contains(name))) {
            return;
        }
        dataBase.getConfig().set(name, value);
        dataBase.save();
    }

    private static String get(final String name) {
        return (name == null || !dataBase.getConfig().contains(name)) ? "" : dataBase.getConfig().getString(name);
    }

    private static boolean getBoolean(final String name) {
        return name != null && dataBase.getConfig().contains(name) && dataBase.getConfig().getBoolean(name);
    }
}
