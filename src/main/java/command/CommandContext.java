package command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class CommandContext {

    private final Event event;
    private final String[] args;

    public CommandContext(GuildMessageReceivedEvent event, String[] args) {
        this.event = event;
        this.args = args;
    }

    public CommandContext(ButtonClickEvent event, String[] args) {
        this.event = event;
        this.args = args;
    }

    // ###############################
    // Event Getters
    // ###############################

    private GuildMessageReceivedEvent getGuildMessageEvent() {
        return (GuildMessageReceivedEvent) event;
    }

    public ButtonClickEvent getButtonEvent() {
        return (ButtonClickEvent) event;
    }

    // ###############################
    // General purpose getters
    // ###############################

    public Guild getGuild() {
        try {
            return getGuildMessageEvent().getGuild();
        } catch (Exception e) {
            return getButtonEvent().getGuild();
        }
    }

    public TextChannel getChannel() {
        try {
            return getGuildMessageEvent().getChannel();
        } catch (Exception e) {
            return getButtonEvent().getTextChannel();
        }
    }

    public String[] getArgs() {
        return args;
    }

    public User getAuthor() {
        try {
            return getGuildMessageEvent().getAuthor();
        } catch (Exception e) {
            return getButtonEvent().getUser();
        }
    }

    public JDA getJDA() {
        try {
            return getGuildMessageEvent().getJDA();
        } catch (Exception e) {
            return getButtonEvent().getJDA();
        }
    }

    public Member getSelfMember() {
        return getGuild().getSelfMember();
    }

    public Member getMember() {
        try {
            return getGuildMessageEvent().getMember();
        } catch (Exception e) {
            return getButtonEvent().getMember();
        }
    }

    public void sendChannelMessage(String s) {
        getChannel().sendMessage(s).queue();
    }

    public void sendChannelMessage(MessageEmbed embed) {
        getChannel().sendMessage(embed).queue();
    }

    public AudioManager getAudioManager() {
        return getGuild().getAudioManager();
    }
}
