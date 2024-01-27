# BackupHandler

BackupHandler is a Minecraft plugin that allows you to save your maps/temporary files and more without any problems.

## Installation


- [Download plugin](https://github.com/eyxp/BackupHandler/actions/runs/7678079635/artifacts/1199547742)
- Add to your Minecraft Spigot Server
- Add your configuration in plugins//Backup//config.json
- [In case of errors, create a Github issue](https://github.com/eyxp/BackupHandler/issues)
- Enjoy the plugin :)
## Config Example

```json
{
  "backupOnStart": true,
  "saveMaps": true,
  "pathToSave": "backups//",
  "backupPattern": "yyyy-MM-dd-HH-mm-ss",
  "ignoreMap": [
    "yourMap"
  ],
  "backUpPaths": [
    "plugins//YourPlugin//saves.yaml"
  ],
  "backupTimes": [
    {
      "hour": 10,
      "minute": 10
    },
    {
      "hour": 0,
      "minute": 0
    }
  ]
}
```

