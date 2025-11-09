package io.github.thebossmagnus.mods.config_manager.neoforge;

import io.github.thebossmagnus.mods.config_manager.common.services.IPlatformHelper;
import net.neoforged.fml.loading.FMLLoader;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class NeoPlatformHelper implements IPlatformHelper {

    @Override
    public Path getGameDir() {
        return FMLLoader.getCurrent().getGameDir();
    }
}