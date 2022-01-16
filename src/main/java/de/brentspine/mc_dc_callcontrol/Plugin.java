package de.brentspine.mc_dc_callcontrol;

import de.brentspine.mc_dc_callcontrol.commands.LinkDiscordCommand;
import de.brentspine.mc_dc_callcontrol.discord.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

public class Plugin extends JavaPlugin {

    public static final String PREFIX = "§2§lCallcontrol §8» §7";
    DiscordApi discordApi;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§fLoading Plugin");

        discordApi = new DiscordApiBuilder()
                .setToken("OTMwMDU5NDc1NjgxOTU1ODgx.YdwXYg.f5yZPZ2mXiZVs0mF4K1HiCUQMmw")
                .setAllIntents()
                .login().join();
        System.out.println(discordApi.isUserCacheEnabled());
        MySQL.connect();

        init();

        Bukkit.getConsoleSender().sendMessage(PREFIX + "§aPlugin ready");
        Bukkit.getConsoleSender().sendMessage(" ");
    }

    private void init() {
        getCommand("linkdiscord").setExecutor(new LinkDiscordCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public DiscordApi getDiscordApi() {
        return discordApi;
    }

}
