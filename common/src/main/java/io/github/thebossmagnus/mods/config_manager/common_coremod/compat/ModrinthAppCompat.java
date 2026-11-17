package io.github.thebossmagnus.mods.config_manager.common_coremod.compat;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static io.github.thebossmagnus.mods.config_manager.common.Constants.LOGGER;

/*
In the Modrinth app, enabling instance options under window size enable Fullscreen.
This will cause Modrinth to generate an options.txt file with Fullscreen enabled which doesn't get overridden by Config Manager.
Fixing this requires a slightly hacky workaround.
When options.txt contains only the launcher's fullscreen setting,
we back it up, let the modpack options.txt be copied, then restore the original file.
*/
public final class ModrinthAppCompat {
    private static boolean isWorkaroundNeeded = false;

    public static void backupOptionsFileIfNeeded(@NotNull Path gameDir) throws IOException {
        Path optionsFile = gameDir.resolve("options.txt");

        if (!Files.exists(optionsFile)) {
            return;
        }

        java.util.List<String> nonBlankLines = Files.readAllLines(optionsFile).stream().map(String::trim).filter(s -> !s.isEmpty()).toList();

        if (nonBlankLines.isEmpty() || nonBlankLines.size() > 2) { //Only the fullscreen setting and the version should be present
            return;
        }

        if (!nonBlankLines.getFirst().startsWith("fullscreen:") && !nonBlankLines.getLast().startsWith("fullscreen:")) {
            return;
        }

        try {
            Path backupFile = gameDir.resolve("options.txt.bak");
            Files.copy(optionsFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
            isWorkaroundNeeded = true;
            Files.delete(optionsFile);
            LOGGER.info("Detected fullscreen preference imposed by the launcher; backing up options.txt before copying modpack defaults");
        } catch (IOException e) {
            throw new RuntimeException("Could not back up options.txt while checking for a Modrinth fullscreen preference", e);
        }
    }

    public static void restoreOptionsFileIfNeeded(@NotNull Path gameDir) {
        if (!isWorkaroundNeeded) {
            return;
        }
        Path backupFile = gameDir.resolve("options.txt.bak");
        if (!Files.exists(backupFile)) {
            return;
        }
        try {
            Files.copy(backupFile, gameDir.resolve("options.txt"), StandardCopyOption.REPLACE_EXISTING);
            Files.delete(backupFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not restore the Modrinth launcher options.txt backup", e);
        }
    }
}
