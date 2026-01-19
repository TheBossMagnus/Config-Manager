package io.github.thebossmagnus.mods.config_manager.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddFlagsUtil {
    private static final AtomicBoolean UPDATE_FLAG = new AtomicBoolean(false);
    private static final AtomicBoolean OVERWRITE_FLAG = new AtomicBoolean(false);


    public static void setUpdateFlag(boolean updateFlag) throws RuntimeException {
        UPDATE_FLAG.set(updateFlag);
        addFlags();
    }


    public static void setOverwriteFlag(boolean overwriteFlag) throws RuntimeException {
        OVERWRITE_FLAG.set(overwriteFlag);
        addFlags();
    }

    private static void addFlags() throws RuntimeException {
        Path configDir;
        try {
            Path gameDir = Services.PLATFORM.getGameDir();
            configDir = gameDir.resolve("config");
        } catch (Exception e) {
            Constants.LOGGER.error("Error while resolving path", e);
            throw new RuntimeException("Error while resolving path: " + e.getMessage(), e);
        }
        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
        } catch (IOException e) {
            Constants.LOGGER.error("Could not create config directory: {}", configDir, e);
            throw new RuntimeException("Could not create config directory", e);
        }
        if (OVERWRITE_FLAG.get()) {
            createFlagFile(configDir, "CONFIG_MANAGER_RESET_FLAG");
        } else if (UPDATE_FLAG.get()) {
            createFlagFile(configDir, "CONFIG_MANAGER_UPDATE_FLAG");
        }
    }

    private static void createFlagFile(Path configDir, String flagFileName) {
        Path flag = configDir.resolve(flagFileName);
        try {
            Files.createFile(flag);
        } catch (IOException e) {
            if (!Files.exists(flag)) {
                Constants.LOGGER.error("Could not create {}: {}", flagFileName, flag, e);
                throw new RuntimeException("Could not create " + flagFileName, e);
            }
            // Ignore if file already exists
        }
    }
}
