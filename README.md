## Important

requires `local.properties` file.

requires `debug.properties` file for `DEBUG` builds (-Pmode=debug).

requires `prod.properties` file for `PROD` builds (-Pmode=prod).

containing the following:

`webhook_url` is the discord webhook url

`minecraft_name_to_discord_id_map` is a comma separated map of minecraft username to discord ids

`config_url` is web link to a google drive sheet (Public Everyone READ ONLY). An example format might look like this:
`https://docs.google.com/spreadsheets/d/________________________/export?exportFormat=csv`
For more information about the contents of this sheet check out the [Remote Config](#remote-config) section below...

E.g....
```
webhook_url=___________
minecraft_name_to_discord_id_map=<user>:<discord_id>,<user>:<discord_id>
config_url=___________
```

So this is an example of a proper user to discrod id:

`benk:12345665432` assuming benk is my minecraft username and 12345665432 is my discord ID

## Update the version 

Simply update the file called

`version.txt` in the root

this will update the version dynamically in the following 2 places:

* ./build.gradle.kts on line 20
* ./src/main/resources/plugin.yml on line 2

once this is done you can perform a build below... (./gradlew make)

## Build Locally a JAR to use

There are 2 variations of builds:

* Production `prod`
* Debug `debug`

If you do NOT pass in the flag to the make command it will throw an error.

For the corresponding build mode (prod/debug) you must ensure you have the correct properties file to provide that environments infomation.

e.g. if you are doing a `-Pmode=debug` build you will need the `debug.properties` file present with the information from [here](#important)

    ./gradlew make -Pmode=debug

    ./gradlew make -Pmode=prod

OR (if you are offline and have the deps cached already)

    ./gradlew --offline make -Pmode=debug

    ./gradlew --offline make -Pmode=prod

This combination task runs the following in order:

```
./gradlew clean
./gradlew assemble
./gradlew shadowJar

```

Now in the `./build/libs` folder you will find the `player-watcher` jar that you can copy into your Paper MC Plugins/ dir

Use the `find . -name *.jar` to locate these files from term

## Remote Config

This section will explain the `config_url` parameter discussed above in the [Important Top Section](#important)

This parameter is a link to a google drive sheets file that has been set to ANYONE and EVERYONE can publicly VIEW the document.

Again here is an example of that url format:
`https://docs.google.com/spreadsheets/d/________________________/export?exportFormat=csv`

Notice: the end of the url is **important** the `export?exportFormat=csv` must be there.

The contents of the sheet is a SINGLE TAB with 2 columns (no headers)

The first column contains "Remote Config Key Names"

The second column contains 0 or 1 values (0 means OFF or FALSE and 1 means ON or TRUE)

These key value pairs act as a remote way to toggle config for this plugins functionality.

For more information about the keys and what their intended functionality is to toggle between, please see the [Remote Config Keys README File](REMOTE_CONFIG_KEYS.md) 

All keys can be found IN CODE in this file: `src/main/kotlin/bk/minecraftplugin/playerWatcherPaperMCPlugin/remote_config/RemoteConfigKeys.kt`

here is an example how the sheet might look:

```
lottery-command 0
player-death-post 0
```
Note: the space in the above example represents a new Cell!

The server will make requests to this public sheet to get (in semi-realtime) the contents of the values here to see if features have been turned off or turned on!

The server requests these values forcefully at these momenets:
* Server start up
* Player join server
* Player death

AGAIN, For more information about the keys and what their intended functionality is to toggle between, please see the [Remote Config Keys README File](REMOTE_CONFIG_KEYS.md)

TODO: I want to add a time to live for this value too, so after X amount of time the server automatically re-fetches the values

test
