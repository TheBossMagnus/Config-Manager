package io.github.thebossmagnus.mods.config_manager.fabric;


import io.github.thebossmagnus.mods.config_manager.common_coremod.ConfigManagerStartup;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.ModContainer;


public class ConfigManagerLanguageAdapter implements LanguageAdapter {

    public ConfigManagerLanguageAdapter() {
        ConfigManagerStartup.run(FabricLoader.getInstance().getGameDir());

    }

    @Override
    public <T> T create(ModContainer mod, String value, Class<T> type) {
        return null;
    }
}
