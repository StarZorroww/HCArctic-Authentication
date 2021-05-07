package com.hcartic.auth.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SQL
{
    public static boolean tableExists(final String table) {
        try {
            final Connection connection = MySQL.getConnection();
            if (connection == null) {
                return false;
            }
            final DatabaseMetaData metadata = connection.getMetaData();
            if (metadata == null) {
                return false;
            }
            final ResultSet rs = metadata.getTables(null, null, table, null);
            if (rs.next()) {
                return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }

    public static boolean insertData(final String columns, final String values, final String table) {
        return MySQL.update("INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ");");
    }

    public static boolean deleteData(final String column, final String logic_gate, String data, final String table) {
        if (data != null) {
            data = "'" + data + "'";
        }
        return MySQL.update("DELETE FROM " + table + " WHERE " + column + logic_gate + data + ";");
    }

    public static boolean exists(final String column, String data, final String table) {
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM " + table + " WHERE " + column + "=" + data + ";");
            if (rs.next()) {
                return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }

    public static boolean deleteTable(final String table) {
        return MySQL.update("DROP TABLE " + table + ";");
    }

    public static boolean truncateTable(final String table) {
        return MySQL.update("TRUNCATE TABLE " + table + ";");
    }

    public static boolean createTable(final String table, final String columns) {
        return MySQL.update("CREATE TABLE IF NOT EXISTS " + table + " (" + columns + ");");
    }

    public static boolean upsert(final String selected, Object object, final String column, String data, final String table) {
        if (object != null) {
            object = "'" + object + "'";
        }
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM " + table + " WHERE " + column + "=" + data + ";");
            if (rs.next()) {
                MySQL.update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + column + "=" + data + ";");
            }
            else {
                insertData(column + ", " + selected, "'" + data + "', '" + object + "'", table);
            }
        }
        catch (Exception ex) {}
        return false;
    }

    public static boolean set(final String selected, Object object, final String column, final String logic_gate, String data, final String table) {
        if (object != null) {
            object = "'" + object + "'";
        }
        if (data != null) {
            data = "'" + data + "'";
        }
        return MySQL.update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + column + logic_gate + data + ";");
    }

    public static boolean set(final String selected, Object object, final String[] where_arguments, final String table) {
        String arguments = "";
        for (final String argument : where_arguments) {
            arguments = arguments + argument + " AND ";
        }
        if (arguments.length() <= 5) {
            return false;
        }
        arguments = arguments.substring(0, arguments.length() - 5);
        if (object != null) {
            object = "'" + object + "'";
        }
        return MySQL.update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + arguments + ";");
    }

    public static Object get(final String selected, final String[] where_arguments, final String table) {
        String arguments = "";
        for (final String argument : where_arguments) {
            arguments = arguments + argument + " AND ";
        }
        if (arguments.length() <= 5) {
            return false;
        }
        arguments = arguments.substring(0, arguments.length() - 5);
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM " + table + " WHERE " + arguments + ";");
            if (rs.next()) {
                return rs.getObject(selected);
            }
        }
        catch (Exception ex) {}
        return null;
    }

    public static List<Object> listGet(final String selected, final String[] where_arguments, final String table) {
        final List<Object> array = new ArrayList<Object>();
        String arguments = "";
        for (final String argument : where_arguments) {
            arguments = arguments + argument + " AND ";
        }
        if (arguments.length() <= 5) {
            return array;
        }
        arguments = arguments.substring(0, arguments.length() - 5);
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM " + table + " WHERE " + arguments + ";");
            while (rs.next()) {
                array.add(rs.getObject(selected));
            }
        }
        catch (Exception ex) {}
        return array;
    }

    public static Object get(final String selected, final String column, final String logic_gate, String data, final String table) {
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM " + table + " WHERE " + column + logic_gate + data + ";");
            if (rs.next()) {
                return rs.getObject(selected);
            }
        }
        catch (Exception ex) {}
        return null;
    }

    public static List<Object> listGet(final String selected, final String column, final String logic_gate, String data, final String table) {
        final List<Object> array = new ArrayList<Object>();
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM " + table + " WHERE " + column + logic_gate + data + ";");
            while (rs.next()) {
                array.add(rs.getObject(selected));
            }
        }
        catch (Exception ex) {}
        return array;
    }

    public int countRows(final String table) {
        int i = 0;
        if (table == null) {
            return i;
        }
        final ResultSet rs = MySQL.query("SELECT * FROM " + table + ";");
        try {
            while (rs.next()) {
                ++i;
            }
        }
        catch (Exception ex) {}
        return i;
    }
}