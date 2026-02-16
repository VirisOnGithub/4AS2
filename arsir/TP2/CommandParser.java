final class CommandParser {
    static ParsedCommand parse(String line) {
        if (line == null)
            return new ParsedCommand("", "");
        String trimmed = line.trim();
        if (trimmed.isEmpty())
            return new ParsedCommand("", "");
        int idx = trimmed.indexOf(' ');
        if (idx < 0) {
            return new ParsedCommand(trimmed.toUpperCase(), "");
        }
        String cmd = trimmed.substring(0, idx).toUpperCase();
        String arg = trimmed.substring(idx + 1).trim();
        return new ParsedCommand(cmd, arg);
    }

    static final class ParsedCommand {
        final String command;
        final String arg;

        ParsedCommand(String command, String arg) {
            this.command = command;
            this.arg = arg;
        }
    }
}
