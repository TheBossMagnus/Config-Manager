package io.github.thebossmagnus.mods.config_manager.common_coremod;


import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.thebossmagnus.mods.config_manager.common_coremod.Constants.LOGGER;

/**
 * Handles config management at startup based on flag files in the config directory.
 */
public final class ConfigManagerStartup {


    /**
     * Chooses config copy strategy based on flag files in config dir.
     */
    public static void run(Path gameDir) {
        Path configDir = gameDir.resolve("config");
        Path resetFlag = configDir.resolve("CONFIG_MANAGER_RESET_FLAG");
        Path updateFlag = configDir.resolve("CONFIG_MANAGER_UPDATE_FLAG");
        try {
            if (Files.exists(resetFlag)) {
                LOGGER.info("Flag detected, running a config reset");
                ResetAndCopyConfig.run(gameDir);
                Files.deleteIfExists(resetFlag);
                Files.deleteIfExists(updateFlag);
            } else if (Files.exists(updateFlag)) {
                LOGGER.info("Flag detected, running a config update");
                OverwriteConfig.run(gameDir);
                Files.deleteIfExists(updateFlag);
            } else {
                LOGGER.info("No flag found, running a usual copy");
                CopyConfig.init(gameDir);
            }
        } catch (Exception e) {
            LOGGER.error("Error during config copy: ", e);
            throw new RuntimeException(e);
        }
    }
}

