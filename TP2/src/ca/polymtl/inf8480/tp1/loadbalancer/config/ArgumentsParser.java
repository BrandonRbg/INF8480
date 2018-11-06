package ca.polymtl.inf8480.tp1.loadbalancer.config;

public class ArgumentsParser {
    public static Config parseArguments(String[] args) {
        try {
            return new Config(
                    args[0],
                    Boolean.parseBoolean(args[1]),
                    args[2],
                    args[3],
                    args[4],
                    Integer.parseInt(args[5])
            );
        } catch (Exception e) {
            return new Config();
        }
    }
}
