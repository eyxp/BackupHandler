package de.eyxp.backup.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.eyxp.backup.Backup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;

public class BackupConfig {

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private BackupObject loadedObject;


    @SneakyThrows
    public Result generateConfig(BackupObject backupObject) {
        String PATH = "plugins//Backup//";
        String FILE_PATH = PATH + "config.json";
        if (isConfigExisting()) {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            StringBuilder stringBuilder = new StringBuilder();
            lines.forEach(stringBuilder::append);
            BackupObject givenObject = GSON.fromJson(stringBuilder.toString(), BackupObject.class);
            this.loadedObject = givenObject;
            return new Result(givenObject, false);
        }
        Backup.getInstance().getLogger().log(Level.INFO, "Generating Backup config...");
        Files.createDirectories(Paths.get(PATH));
        Files.createFile(Paths.get(FILE_PATH));
        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            fileWriter.write(GSON.toJson(backupObject));
        }
        this.loadedObject = backupObject;
        return new Result(backupObject, true);
    }

    public boolean isConfigExisting() {
        return new File("plugins//Backup//config.json").exists();
    }

    public BackupObject getConfig() {
        return loadedObject;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result {
        final BackupObject backupObject;
        final boolean firstStart;
    }

    @RequiredArgsConstructor
    @Getter
    public static class TimeInfo {
        final int hour;
        final int minute;
    }
}
