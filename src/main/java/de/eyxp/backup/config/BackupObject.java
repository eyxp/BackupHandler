package de.eyxp.backup.config;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BackupObject {

    public boolean backupOnStart;
    public boolean saveMaps;
    public String pathToSave;
    public String backupPattern;
    public String[] ignoreMap;
    public String[] backUpPaths;
    public BackupConfig.TimeInfo[] backupTimes;
}
