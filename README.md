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

## Update the version 

You need to update the version of the lib in 2 places:

* ./build.gradle.kts on line 11
* ./src/main/resources/plugin.yml on line 2

once this is done you can perform a build below...

## Build Locally a JAR to use

    ./gradlew make

This combination task runs the following in order:

```
./gradlew clean
./gradlew assemble
./gradlew shadowJar

```

Now in the `./build/libs` folder you will find the `player-watcher` jar that you can copy into your Paper MC Plugins/ dir

Use the `find . -name *.jar` to locate these files from term

