## Important

requires `local.properties` file

containing the following:

`webhook_url` is the discord webhook url

`minecraft_name_to_discord_id_map` is a comma separated map of minecraft username to discord ids

E.g....
```
webhook_url=___________
minecraft_name_to_discord_id_map=<user>:<discord_id>,<user>:<discord_id>
```

So this is an example of a proper user to discrod id:

`benk:12345665432` assuming benk is my minecraft username and 12345665432 is my discord ID

## Build Locally a JAR to use

Follow these commands:

```
./gradlew clean
./gradlew assemble
./gradlew shadowJar
```

Now in the `./build/libs` folder you will find the `player-watcher` jar that you can copy into your Paper MC Plugins/ dir