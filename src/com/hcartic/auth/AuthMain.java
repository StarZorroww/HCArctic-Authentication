package com.hcartic.auth;

import com.hcartic.auth.database.MySQL;
import com.hcartic.auth.database.SQL;
import com.hcartic.auth.managers.AManager;
import com.hcartic.auth.managers.LManager;
import com.hcartic.auth.utils.ConfigFile;
import com.hcartic.auth.utils.DBConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;


public class AuthMain extends JavaPlugin {

    private Logger logger;

    private static ConfigFile database;

    private Map<UUID, List<String>> documentation;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.logger = this.getLogger();
        database = new ConfigFile("database.yml", this);
        DBConfigFile.create();

        //SQL start logic
        MySQL.connect();

        // Check if the SQL connection wasn't made
        if (!MySQL.isConnected()) {
            logger.severe("[Authentication] Database connection wasn't made, maybe check database.yml??? Disabling!");
            getPluginLoader().disablePlugin(this);
            return;
        }

        this.documentation = new HashMap<>();

        String query = "";

        // Check if the required table doesn't exist...
        if (!SQL.tableExists("documentation")) {
            // Create the documentation table because it doesn't exist
            query = "CREATE TABLE documentation (uuid varchar(256), code varchar(256), ip varchar(256))";
            MySQL.update(query);
        } else {
            // Create the query command to select every row
            query = "SELECT * FROM documentation";
            ResultSet rs = MySQL.query(query);
            try {
                while (rs.next()) {
                    /* Setup the documentation list with the rows column data. We do this to prevent
                     * accessing the mysql 24/7. It's way more efficient to save it in memory then
                     * creating query and update commands for the mysql all the time.
                     *
                     * TL;DR: it's faster to do things this way..
                     */
                    List<String> data = new ArrayList<>();

                    data.add(0, rs.getString("code"));
                    data.add(1, rs.getString("ip"));

                    // Now put all the documentated data into the map for later use!!
                    documentation.put(UUID.fromString(
                            rs.getString("uuid")), data);
                }

            } catch (SQLException e) {
                // This will most likely be the outcome for a while... lol
                logger.severe("[Authentication] Error: " + e.getMessage());
            }
        }

        AManager aManager = new AManager(this, this.documentation);
        LManager lManager = new LManager(this, aManager);
        lManager.registerListeners();

        logger.info("[Authentication] Enabled");
    }

    @Override
    public void onDisable() {
        AManager.disable();
        logger.info("[Authentication] Disabled");
    }

    public static ConfigFile getDataBaseConfig() {
        return database;
    }
}
