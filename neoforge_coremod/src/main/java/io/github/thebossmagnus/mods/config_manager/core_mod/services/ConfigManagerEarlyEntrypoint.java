/**
 * Adapted from https://github.com/KostromDan/Crash-Assistant, LGPL-3.0
 */


package io.github.thebossmagnus.mods.config_manager.core_mod.services;


import io.github.thebossmagnus.mods.config_manager.common_coremod.ConfigManagerStartup;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforgespi.earlywindow.GraphicsBootstrapper;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * The mod should be launched as soon as possible after game start
 * to be able to help players even with coremod/mixin/hs_err crashes.
 * So we launch it from the constructor of the GraphicsBootstrapper, the first point, we can launch it from neoforge.
 */
public class ConfigManagerEarlyEntrypoint implements GraphicsBootstrapper {


    public ConfigManagerEarlyEntrypoint() {
        Path gameDir;
        // Hacky reflection to keep everything in a single jar
        // 1.21.9+: FMLLoader.getCurrent().getGameDir()
        // older version: FMLLoader.getGamePath()
        try {

            Object loader = FMLLoader.class.getMethod("getCurrent").invoke(null);
            gameDir = (Path) loader.getClass().getMethod("getGameDir").invoke(loader);
        } catch (NoSuchMethodException e) {
            // Fallback to the older method if new one doesn't exist
            try {
                gameDir = (Path) FMLLoader.class.getMethod("getGamePath").invoke(null);
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException("Unable to obtain game directory from FMLLoader", ex);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to obtain game directory from FMLLoader", e);
        }
        ConfigManagerStartup.run(gameDir);

    }

    @Override
    public @NotNull String name() {
        return "config_manager";
    }

    @Override
    public void bootstrap(String[] arguments) {

    }

}
