package io.github.thebossmagnus.mods.config_manager.common_coremod.compat;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static io.github.thebossmagnus.mods.config_manager.common.Constants.LOGGER;

/*
In the Modrinth app, enabling instance options under window size enable Fullscreen.
This will cause Modrinth to generate an options.txt file with Fullscreen enabled which doesn't get overridden by Config Manager.
Fixing this requires a slightly hacky workaround.
*/
public final class ModrinthAppCompat {
    private static boolean isWorkaroundNeeded = false;
    private static boolean fullscreenEnabled;

    public static void backupOptionsFileIfNeeded(@NotNull Path gameDir) throws IOException {
        Path optionsFile = gameDir.resolve("options.txt");


        if (!Files.exists(optionsFile)) {
            return;
        }

        java.util.List<String> nonBlankLines = Files.readAllLines(optionsFile).stream().map(String::trim).filter(s -> !s.isEmpty()).toList();

        if (nonBlankLines.isEmpty() || nonBlankLines.size() > 2) { //Only the fullscreen setting and the version should be present
            return;
        }

        if (nonBlankLines.getFirst().startsWith("fullscreen:")) {
            fullscreenEnabled = Boolean.parseBoolean(nonBlankLines.getFirst().split(":")[1]);
        } else if (nonBlankLines.getLast().startsWith("fullscreen:")) {
            fullscreenEnabled = Boolean.parseBoolean(nonBlankLines.getLast().split(":")[1]);
        } else {
            return;
        }

        try {
            isWorkaroundNeeded = true;
            Files.delete(optionsFile);
            LOGGER.info("Detected fullscreen preference imposed by the launcher, using workaround to preserve it ({}).", fullscreenEnabled);
        } catch (IOException e) {
            throw new RuntimeException("Could not back up options.txt while checking for a Modrinth fullscreen preference", e);
        }
    }

    public static void restoreOptionsFileIfNeeded(@NotNull Path gameDir) {
        if (!isWorkaroundNeeded) {
            return;
        }
        try {
            Path optionsFile = gameDir.resolve("options.txt");
            List<String> lines = Files.exists(optionsFile) ? new ArrayList<>(Files.readAllLines(optionsFile).stream().map(String::trim).filter(s -> !s.isEmpty()).toList()) : new ArrayList<>();
            // Remove any existing fullscreen setting
            lines.removeIf(line -> line.startsWith("fullscreen:"));
            lines.add("fullscreen:" + fullscreenEnabled);
            Files.write(optionsFile, lines);

            LOGGER.info("Restored options.txt with fullscreen preference: {}", fullscreenEnabled);
        } catch (IOException e) {
            throw new RuntimeException("Could not restore the Modrinth launcher options.txt backup", e);
        }
    }
}
