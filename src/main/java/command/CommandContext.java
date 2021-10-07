package command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandContext {

    private final GuildMessageReceivedEvent event;
    private final String[] args;

    public CommandContext(GuildMessageReceivedEvent event, String[] args) {
        this.event = event;
        this.args = args;
    }

    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    public String[] getArgs() {
        return args;
    }
}
