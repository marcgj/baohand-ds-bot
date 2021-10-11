package command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.nio.channels.Channel;

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

    // TODO: test for errors
    public GuildMessageReceivedEvent getEvent() {
        return (GuildMessageReceivedEvent) event;
    }
    public Guild getGuild(){
        try {
            return getEvent().getGuild();
        }catch (Exception e) {
            return getButtonEvent().getGuild();
        }
    }

    public TextChannel getChannel(){
        try {
            return getEvent().getChannel();
        }catch (Exception e) {
            return getButtonEvent().getTextChannel();
        }
    }

    public ButtonClickEvent getButtonEvent() {
        return (ButtonClickEvent) event;
    }

    public String[] getArgs() {
        return args;
    }
}
