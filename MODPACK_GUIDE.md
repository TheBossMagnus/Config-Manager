# Modpack Developer Guide

Setting up Config Manager in your modpack is straightforward:

1. **Add Config Manager** to your modpack. You can find it on [Modrinth](https://modrinth.com/mod/configmanager) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/configmanager).
2. **Create a `modpack_defaults` folder** inside your Minecraft `config` directory.
3. **Copy your default configuration files** into this folder, preserving the structure of the Minecraft directory.

The `modpack_defaults` folder should mirror the structure of the Minecraft root directory. For example:

```text
.minecraft/
├── 📄 options.txt                  # User's options
├── 📁 config/
│   ├── 📄 myModConfig.json                # User's mod configuration
│   └── 📁 modpack_defaults/
│       ├── 📄 options.txt            # Modpack default options
│       └── 📁 config/
│           └── 📄 myModConfig.json        # Modpack default mod configuration
```

See this [example implementation](https://github.com/TheBossMagnus/Thunder/tree/main/src) in my modpack, Thunder.

---

## FAQ

**❓ Can I forcibly override user preferences for a specific file?**

Technically, yes. Place the file directly in the Minecraft `config` directory, outside `modpack_defaults`. Files placed here take precedence over the modpack defaults.

**❓ Can I force an update or reset of user configurations?**

Yes. You can trigger these actions by placing one of the following flag files in the `config` directory:

- `CONFIG_MANAGER_UPDATE_FLAG` — forces a configuration update (syncs user settings with the modpack defaults).
- `CONFIG_MANAGER_RESET_FLAG` — forces a configuration reset (deletes all user preferences and syncs settings with the modpack defaults).

Players can perform these actions themselves via the mod's settings. If you need either of these actions, the recommended approach is to instruct the player to **trigger them manually**.

>[!CAUTION]
>**However, this is not recommended**, as it may overwrite user customizations without their approval.

**❓ Can I use Config Manager for content other than configuration files (e.g., `.jar` files)?**

Technically, yes. There are no restrictions on the types of files you can place in `modpack_defaults`.

>[!CAUTION]
>However, this is **not recommended**. Config Manager is designed to manage configuration files; using it for other types of content can lead to unexpected behavior and may confuse the player.
