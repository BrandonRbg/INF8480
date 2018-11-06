package ca.polymtl.inf8480.tp2.calculationsserver.config;

public class ArgumentsParser {
    public static Config parseArguments(String[] args) {
        try {
            return new Config(
                    Integer.parseInt(args[0]),
                    Float.parseFloat(args[1]),
                    args[2],
                    Integer.parseInt(args[3]),
                    args[4]
            );
        } catch (Exception e) {
            return new Config();
        }
    }
}
