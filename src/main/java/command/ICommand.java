package command;

public interface ICommand {

    void handle(CommandContext ctx);

    String getName();



}
