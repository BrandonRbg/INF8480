package ca.polymtl.inf8480.tp1.calculationserver.config;

public class ArgumentsParser {
    public static Config parseArguments(String[] args) {
        try {
            return new Config(
                    Integer.parseInt(args[0]),
                    Float.parseFloat(args[1]),
                    args[2],
                    Integer.parseInt(args[3])
            );
        } catch (Exception e) {
            return null;
        }
    }
}
