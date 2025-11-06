package io.github.thebossmagnus.mods.config_manager.neoforge;

import io.github.thebossmagnus.mods.config_manager.common.services.IPlatformHelper;
import net.neoforged.fml.loading.FMLLoader;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class NeoPlatformHelper implements IPlatformHelper {

    @Override
    public Path getGameDir() {
        // Hacky reflection to keep everything in a single jar
        // 1.21.9+: FMLLoader.getCurrent().getGameDir()
        // older version: FMLLoader.getGamePath()
        try {

            Object loader = FMLLoader.class.getMethod("getCurrent").invoke(null);
            return (Path) loader.getClass().getMethod("getGameDir").invoke(loader);
        } catch (NoSuchMethodException e) {
            // Fallback to the older method if new one doesn't exist
            try {
                return (Path) FMLLoader.class.getMethod("getGamePath").invoke(null);
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException("Unable to obtain game directory from FMLLoader", ex);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to obtain game directory from FMLLoader", e);
        }
    }
}