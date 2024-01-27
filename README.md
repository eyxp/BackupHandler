
# BackupHandler

BackupHandler is a Minecraft plugin that allows you to save your maps/temporary files and more without any problems.


## Features

- Always have a backup in case of data loss
- Select individual or entire folders
- Set a time for a backup
- No server impairment
- Best performance
- 100% configurable
## FAQ

#### Minecraft Version?

- Version-independent

#### Java Version?

- Version-independent


## Installation


- [Download plugin](https://github.com/eyxp/BackupHandler/actions/runs/7678316701/artifacts/1199576990)
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

