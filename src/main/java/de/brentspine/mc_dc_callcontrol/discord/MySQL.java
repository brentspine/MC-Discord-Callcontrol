package de.brentspine.mc_dc_callcontrol.discord;

import de.brentspine.mc_dc_callcontrol.Plugin;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    public static String host = "localhost";
    public static String port = "3306";
    public static String database = "links";
    public static String user = "root";
    public static String password = "craftmine-01";
    public static Connection con;
    public static Connection gameCon;

    public static void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
                Bukkit.getConsoleSender().sendMessage(Plugin.PREFIX + "Es wurde eine Verbindung mit der Datenbank§8(§c" + database + "§8) §7aufgebaut §8(§c" + host + "§7:§c" + port + "§8)");
            } catch (SQLException var1) {
                Bukkit.getConsoleSender().sendMessage(Plugin.PREFIX + "Es konnte keine Verbindung mit der Datenbank aufgebaut werden");
            }
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                Bukkit.getConsoleSender().sendMessage(Plugin.PREFIX + "Verbundung geschlossen");
            } catch (SQLException var1) {
                var1.printStackTrace();
            }
        }
    }



    public static boolean isConnected() {
        return con != null;
    }

    public static Connection getConnection() {
        return con;
    }

    public static boolean gameIsConnected() {
        return gameCon != null;
    }

    public static Connection getGameConnection() {
        return gameCon;
    }

}
