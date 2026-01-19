# Config Manager

Download it from:

* [Modrinth](https://modrinth.com/mod/configmanager)
* [CurseForge](https://curseforge.com/minecraft/mc-mods/configmanager)

**Config Manager** is a Minecraft mod that allows modpack developers to ship configuration files in a simple and
user-friendly way.  
It supports both **NeoForge** and **Fabric**, starting from **Minecraft 1.20.1** and newer.

---

## Features

### Ship modpack defaults without overriding user preferences

- Place your modpack’s default configuration files in `/config/modpack_defaults`.
- At startup, all files from that directory are copied into the Minecraft directory **only if they do not already exist
  **.

**Examples:**

- `.minecraft/config/modpack_defaults/options.txt` → copied to `.minecraft/options.txt`
- `.minecraft/config/modpack_defaults/config/myMod.json` → copied to `.minecraft/config/myMod.json`

This ensures that user settings are preserved across modpack updates.

---

### Update or reset to the modpack’s defaults

From the mod list (NeoForge) or Mod Menu (Fabric), you can open the Config Manager GUI and choose between two actions:

1. **Update** – Copies (overwriting) the modpack’s config files on top of the user’s.
    - All settings changed by the pack are updated.
    - Untouched settings remain as configured by the user.

2. **Reset** – Wipes the entire `config` directory and replaces it with the files shipped by the modpack.
    - Useful to fix broken or corrupted installations.

---

