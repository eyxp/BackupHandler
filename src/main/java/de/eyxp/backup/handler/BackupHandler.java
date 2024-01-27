package de.eyxp.backup.handler;

import de.eyxp.backup.Backup;
import de.eyxp.backup.config.BackupConfig;
import de.eyxp.backup.config.BackupObject;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class BackupHandler {

    @SneakyThrows
    public void handleBackup() {
        double start = System.currentTimeMillis();
        BackupObject backupObject = Backup.getInstance().getBackupConfig().getConfig();
        if (!new File(backupObject.pathToSave).exists()) Files.createDirectories(Paths.get(backupObject.pathToSave));
        File backupFile = new File(LocalDateTime.now().format(DateTimeFormatter.ofPattern(backupObject.backupPattern)));
        File file = new File(backupObject.pathToSave + backupFile.getPath());
        if (file.exists()) {
            Backup.getInstance().getLogger().log(Level.WARNING, "Try to generate file with same name! " + file.getPath());
            return;
        }
        Files.createDirectories(Paths.get(file.getPath()));
        if (backupObject.saveMaps) {
            for (World world : Bukkit.getWorlds()) {
                if (Arrays.stream(backupObject.ignoreMap).anyMatch(name -> name.equalsIgnoreCase(world.getName())))
                    return;
                File worldFile = new File(world.getName());
                copyToDestination(new File(backupFile.getPath() + "//" + world.getName()), worldFile, backupObject);
            }
        }
        for (String path : backupObject.backUpPaths) {
            copyToDestination(new File(backupFile.getPath() + "//" + path), new File(path), backupObject);
        }
        Backup.getInstance().getLogger().log(Level.INFO, "Finished backup after " + (System.currentTimeMillis() - start) + "ms '" + backupFile.getPath() + "'");
    }

    @SneakyThrows
    public void copyToDestination(File backupFile, File targetFile, BackupObject backupObject) {
        Files.walk(Paths.get(targetFile.getPath()))
                .forEach(source -> {
                    Path destination = Paths.get(backupObject.pathToSave + backupFile.getPath()).resolve(Paths.get(targetFile.getPath()).relativize(source));
                    try {
                        if (Files.isDirectory(source)) {
                            Files.createDirectories(destination);
                        } else {
                            if (!Files.exists(destination.getParent())) {
                                Files.createDirectories(destination.getParent());
                            }
                            if (!Files.exists(destination)) {
                                Files.createFile(destination);
                            }
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        if (source.getFileName().toString().equalsIgnoreCase("session.lock")) return;
                        Backup.getInstance().getLogger().log(Level.WARNING, "Error during copying '" + source + "' ");
                    }
                });
    }

    public void listenForTime(BackupConfig.TimeInfo timeInfo) {
        Timer timer = new Timer();
        Calendar now = Calendar.getInstance();
        Calendar scheduledTime = Calendar.getInstance();
        scheduledTime.set(Calendar.HOUR_OF_DAY, timeInfo.getHour());
        scheduledTime.set(Calendar.MINUTE, timeInfo.getMinute());
        scheduledTime.set(Calendar.SECOND, 0);
        if (now.after(scheduledTime)) {
            scheduledTime.add(Calendar.HOUR_OF_DAY, 24);
        }
        long delayMillis = scheduledTime.getTimeInMillis() - now.getTimeInMillis();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handleBackup();
                new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                        listenForTime(timeInfo);
                        timer.cancel();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        };
        timer.schedule(task, delayMillis);
    }
}
