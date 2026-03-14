import java.util.ArrayList;

public class Command {
    private final CommandType type;
    private final ArrayList<String> args;

    public Command(CommandType type, ArrayList<String> args){
        this.type = type;
        this.args = args;
    }

    public static Command parseCommand(String commandStr) {
        String[] parts = commandStr.split(" ");
        CommandType type = switch (parts[0].toUpperCase()) {
            case "USER" -> CommandType.USER;
            case "PASS" -> CommandType.PASS;
            case "STAT" -> CommandType.STAT;
            case "LIST" -> CommandType.LIST;
            case "RETR" -> CommandType.RETR;
            case "TOP" -> CommandType.TOP;
            case "DELE" -> CommandType.DELE;
            case "RSET" -> CommandType.RSET;
            case "NOOP" -> CommandType.NOOP;
            case "QUIT" -> CommandType.QUIT;
            default -> throw new IllegalArgumentException("Unknown command: " + parts[0]);
        };
        return new Command(type, new ArrayList<>(java.util.Arrays.asList(parts).subList(1, parts.length)));
    }
}
