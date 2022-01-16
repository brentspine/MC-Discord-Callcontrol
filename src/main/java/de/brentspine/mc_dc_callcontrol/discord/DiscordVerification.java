package de.brentspine.mc_dc_callcontrol.discord;

import de.brentspine.mc_dc_callcontrol.Plugin;
import org.bukkit.entity.Player;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.core.entity.permission.RoleImpl;

import javax.print.attribute.standard.Media;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class DiscordVerification {

    private Player player;
    private String userID;
    private Plugin plugin;
    private DiscordApi api;
    private TextChannel channel;

    private User user;
    private Message message;
    private ReactionAddListener reactionListener;
    public static final String VERIFICATION_CHANNEL_ID = "930095370447499285";

    public DiscordVerification(Player player, String userID, Plugin plugin, DiscordApi api) throws ExecutionException, InterruptedException {
        this.player = player;
        this.userID = userID;
        this.plugin = plugin;
        this.api = api;
        this.user = api.getUserById(userID).get();
        this.channel = api.getChannelById(VERIFICATION_CHANNEL_ID).get().asTextChannel().get();
        sendMessage();
        addListener();
    }

    private void sendMessage() throws ExecutionException, InterruptedException {
        user.addRole(channel.asServerChannel().get().getServer().getRoleById(930146425336115230l).get());
        message = channel.sendMessage("**Verification " + player.getName() + "**\n" +
                "<@" + userID + "> please verify the link with Minecraft account " + player.getName() + " by reacting with :white_check_mark:\n" +
                "Abgeschlossen: \uD83D\uDEAB").get();
        message.addReaction("✅");
    }

    private void addListener() {
        reactionListener = api.addReactionAddListener(event -> {
            if(!event.getChannel().getIdAsString().equalsIgnoreCase(VERIFICATION_CHANNEL_ID)) return;
            if(event.getMessage().get().getId() != message.getId()) return;
            if(!event.getUser().get().getIdAsString().equalsIgnoreCase(userID)) return;
            MySQLDiscordLink.setId(player.getUniqueId(), Long.valueOf(userID));
            try {
                player.sendMessage(Plugin.PREFIX + "§aDiscord Account §2" + api.getUserById(userID).get().getDiscriminatedName() + "§a wurde verbunden");
            } catch (Exception e) {
                e.printStackTrace();
            }
            message.edit("**Verification " + player.getName() + "**\n" +
                    "<@" + userID + "> please verify the link with Minecraft account " + player.getName() + " by reacting with :white_check_mark:\n" +
                    "Abgeschlossen: ✅").join();
            user.removeRole(channel.asServerChannel().get().getServer().getRoleById(930146425336115230l).get());
            api.removeListener(reactionListener);
        }).getListener();

    }

    public Message getMessage() {
        return message;
    }

    public Player getPlayer() {
        return player;
    }

    public String getUserID() {
        return userID;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public DiscordApi getApi() {
        return api;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public User getUser() {
        return user;
    }

}
