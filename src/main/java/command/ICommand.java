package command;

public interface ICommand {

    void handle(CommandContext ctx);

    String getName();

    default boolean adminCommand(){
        return false;
    }

    default String getShort(){
        return null;
    }

    default String getHelp() { return "Apartat help no implementada en aquesta comanda"; }
}
