package de.brentspine.mc_dc_callcontrol.discord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLDiscordLink {

    //Checks if an entry with the given uuid exists in the table
    public static boolean isUserExisting(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM discord WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    //Checks if a user needs to be added before creating the entry by calling the createUser method
    public static boolean createUserIfNeeded(UUID uuid, Long discordID) {
        if (!isUserExisting(uuid)) {
            try {
                createUser(uuid, discordID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    //Creates an entry in the table
    public static void createUser(UUID uuid, Long discordID) {
        try {
            PreparedStatement ps;
            ps = MySQL.con.prepareStatement("INSERT INTO discord (uuid, id) VALUES (?,?)");
            ps.setString(1, uuid.toString());
            ps.setLong(2, discordID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setId(UUID uuid, Long discordID) {
        if(createUserIfNeeded(uuid, discordID)) return;
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("UPDATE id SET discord = ? WHERE uuid = ?");
            ps.setLong(1, discordID);
            ps.setString(2, uuid.toString());
        } catch (SQLException e) {
            return;
        }
    }

    public static Long getId(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT id FROM discord WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.getLong("id");
        } catch (SQLException e) {
            return -1l;
        }
    }

    public static Long getUuid(Long id) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT uuid FROM discord WHERE id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.getLong("uuid");
        } catch (SQLException e) {
            return -1l;
        }
    }

}
