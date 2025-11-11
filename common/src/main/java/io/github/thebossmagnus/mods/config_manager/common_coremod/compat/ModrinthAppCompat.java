package io.github.thebossmagnus.mods.config_manager.common_coremod.compat;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static io.github.thebossmagnus.mods.config_manager.common.Constants.LOGGER;

/*
In the Modrinth app, enabling instance options under window size enable Fullscreen.
This will cause Modrinth to generate an options.txt file with Fullscreen enabled which doesn't get overridden by Config Manager.
Fixing this requires a slightly hacky workaround.
The launcher option will override the pack preference (only for the full screen obv)
To do that we first check if the file, that only "fullscreen:..."  exits or not (-1), than we save the status of fullscreen (0 or 1),
delete the file, copy and later reapply it after the copy
*/
public final class ModrinthAppCompat {


    public static int getFullScreenSetting(@NotNull Path gameDir) {

        //check if the file content equals fullscreen:true
        if (Files.exists(gameDir.resolve("options.txt"))) {
            try {
                String content = Files.readString(gameDir.resolve("options.txt"));
                if (content.equals("\nfullscreen:true") || content.equals("fullscreen:true")) {
                    LOGGER.info("Detected fullscreen preference imposed by the launcher: true, Merging it with modpack preference");
                    return 1;
                } else if (content.equals("\nfullscreen:false") || content.equals("fullscreen:false")) {
                    LOGGER.info("Detected fullscreen preference imposed by the launcher: false, Merging it with modpack preference");
                    return 0;
                }
            } catch (Exception e) {
                return -1;
            }
        }

        return -1;
    }

    public static void reapplyFullScreenSetting(@NotNull Path gameDir, int status) {
        if (status == -1) {
            return;
        }
        String fullscreenSetting = (status == 1) ? "fullscreen:true" : "fullscreen:false";

        try {
            Path optionsFile = gameDir.resolve("options.txt");
            java.util.List<String> lines = Files.readAllLines(optionsFile);
            boolean replaced = false;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("fullscreen:")) {
                    if (!lines.get(i).equals(fullscreenSetting)) {
                        LOGGER.warn("Overriding modpack fullscreen preference ({}) with launcher preference ({})", lines.get(i), fullscreenSetting);
                    }
                    lines.set(i, fullscreenSetting);
                    replaced = true;
                    break;
                }
            }
            if (!replaced) {
                lines.add(fullscreenSetting);
            }
            Files.write(optionsFile, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            LOGGER.error("Could not reapply fullscreen preference chosen by the launcher", e);
        }
    }
}
