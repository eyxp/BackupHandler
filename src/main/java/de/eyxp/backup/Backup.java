package de.eyxp.backup;

import de.eyxp.backup.config.BackupConfig;
import de.eyxp.backup.config.BackupObject;
import de.eyxp.backup.handler.BackupHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Backup extends JavaPlugin {


    @Getter
    private static Backup instance;
    @Getter
    private BackupConfig backupConfig;

    @Override
    public void onEnable() {
        instance = this;

        this.backupConfig = new BackupConfig();
        BackupHandler backupHandler = new BackupHandler();

        BackupObject backupObject = new BackupObject(true,
                true,
                "backups//",
                "yyyy-MM-dd-HH-mm-ss",
                new String[]{"yourMap"},
                new String[]{"plugins//YourPlugin//saves.yaml"},
                new BackupConfig.TimeInfo[]{
                        new BackupConfig.TimeInfo(10, 10),
                        new BackupConfig.TimeInfo(0, 0),
                }
        );

        {
            if (backupConfig.generateConfig(backupObject).isFirstStart()) {
                this.getLogger().log(Level.INFO, "First start recognized, please configure backup plugin in plugins//Backup//config.json");
                return;

            }

            if (backupConfig.getConfig().backupTimes.length != 0) {
                for (BackupConfig.TimeInfo timeInfo : backupConfig.getConfig().backupTimes) {
                    backupHandler.listenForTime(timeInfo);
                }
                this.getLogger().log(Level.INFO, "Setting up " + backupConfig.getConfig().backupTimes.length + " time listener!");
            }
            if (backupConfig.getConfig().backupOnStart) {
                backupHandler.handleBackup();
            }
        }
    }
}
