package ca.polymtl.inf8480.tp1.client.utils;

import java.io.IOException;

public class ConfigManager {
    private static Config config;

    static {
        try {
            config = Config.getFromFile();
        } catch (IOException | ClassNotFoundException ignored) {
        }
    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config conf) {
        config = conf;
    }
}
