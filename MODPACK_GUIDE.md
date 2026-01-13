# Modpack Developer Guide

Setup is straightforward:
1. Add Config Manager to your modpack (from [Modrinth] or [CurseForge]).
2. Copy all configuration files into a folder called `modpack_default` inside `config/`.
3. The structure inside `modpack_default` should mirror the root of the Minecraft directory.

**Example:**
```
.minecraft/
├─ options.txt (the "real" one)
├─ config/
│ ├─ myMod.json (the modpack one)
│ ├─ modpack_default/
│ │ ├─ options.txt (the modpack one)
│ │ └─ config/
│ │ └─ myMod.json (the "real" one)
```
This is an [example implementation](https://github.com/TheBossMagnus/Thunder) (my modpack).

---

## FAQ

**Q: Can I forcefully override user preferences for a specific file?**  
**A:** Yes. Place the file directly in its normal location instead of only inside `modpack_default`.

---

**Q: Can I force an update or reset of user configs?**  
**A:** You can do this by placing a file named `CONFIG_MANAGER_UPDATE_FLAG` or `CONFIG_MANAGER_RESET_FLAG` in the `config` directory.  
**Still, this is not recommended.**

---

**Q: Can I use this for other content than config files (e.g., .jar files)?**  
**A:** Technically yes, there are no restrictions.  
**Not recommended** as this mod is not designed for that, and it may confuse users.
