package de.brentspine.mc_dc_callcontrol.commands;

import de.brentspine.mc_dc_callcontrol.Plugin;
import de.brentspine.mc_dc_callcontrol.discord.DiscordVerification;
import de.brentspine.mc_dc_callcontrol.discord.MySQLDiscordLink;
import de.brentspine.mc_dc_callcontrol.util.ClickableMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.DiscordApi;

import java.util.concurrent.ExecutionException;

public class LinkDiscordCommand implements CommandExecutor {

    private Plugin plugin;
    private DiscordApi discordAPI;
    private Server server;
    public static final String INVITE = "https://discord.gg/X5cZdAXK";

    public LinkDiscordCommand(Plugin plugin) {
        this.plugin = plugin;
        discordAPI = plugin.getDiscordApi();
        server = discordAPI.getChannelById(DiscordVerification.VERIFICATION_CHANNEL_ID).get().asServerChannel().get().getServer();
        System.out.println("updated");
        discordAPI.addReactionAddListener(event -> {
            if(!event.getChannel().getIdAsString().equalsIgnoreCase(DiscordVerification.VERIFICATION_CHANNEL_ID)) return;
            if(event.getUser().get().isYourself()) return;
            event.removeReaction();
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if(args.length < 1) {
            player.sendMessage(Plugin.PREFIX + "§cUsage: /linkdiscord <UserTag>");
            player.sendMessage(Plugin.PREFIX + "§cExample: /linkdiscord Brentspine#0072");
            return true;
        }
        boolean exists = false;
        for(Server server : discordAPI.getServers()) {
            for(User user : server.getMembers()) {
                if(user.getDiscriminatedName().equalsIgnoreCase(args[0])) {
                    try {
                        exists = true;
                        DiscordVerification discordVerification = new DiscordVerification(player, user.getIdAsString(), plugin, discordAPI);
                        player.spigot().sendMessage(new ClickableMessage(Plugin.PREFIX).addText(ChatColor.GOLD + "Please verify the action by reacting to ").addClickHoverEvent( ChatColor.GOLD.toString() + ChatColor.BOLD + "this", ClickEvent.Action.OPEN_URL, discordVerification.getMessage().getLink().toString(), HoverEvent.Action.SHOW_TEXT, ChatColor.GREEN + "Click to open").addText(ChatColor.GOLD + " message").build());
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(!exists)
            player.sendMessage(Plugin.PREFIX + "§cPlease join this server and try again: §6" + INVITE);
        return false;
    }

}
