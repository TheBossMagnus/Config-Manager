package io.github.thebossmagnus.mods.config_manager.common_coremod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * Copies config files from modpack_defaults, overwriting any existing files in the config directory.
 */
public final class OverwriteConfig {


    public static void run(Path gameDir) {
        Path configDir = gameDir.resolve("config");
        Path defaultsDir = configDir.resolve(Constants.DIR_NAME);
        if (!Files.exists(defaultsDir)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(defaultsDir)) {
            paths.filter(path -> !path.equals(defaultsDir)).forEach(source -> {
                Path relative = defaultsDir.relativize(source);
                Path target = gameDir.resolve(relative);
                try {
                    if (Files.isDirectory(source)) {
                        Files.createDirectories(target);
                    } else {
                        Files.createDirectories(target.getParent());
                        // Overwrite existing files
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error copying " + source + " to " + target, e);
                }
            });
            Constants.LOGGER.info("Config files updated");
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy modpack_defaults", e);
        }
    }
}
