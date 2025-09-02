package bk.minecraftplugin.deaths

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent

/**
 * Open the minecraft folder on your machine
 *
 * $HOME/Library/Application Support/minecraft/versions/1.21.8
 * find the jar can unzip it...
 * then locate the localization file
 * ./assets/minecraft/lang/en_us.json
 * find all the values that start with "death..."
 * copy that content into a tmp file (tmp.txt)
 * run the script: ./script/parseLocalisationDeaths.sh tmp.txt with the file to generate the map contents below
 */

/**
 * 1 == player name who died
 * 2 == entity name that killed him
 * 3 == item that did it?
 */
val mapOfDeathMessages = mapOf<String, Int>(
    "death.attack.anvil" to 1,
    "death.attack.anvil.player" to 2,
    "death.attack.arrow" to 2,
    "death.attack.arrow.item" to 3,
    "death.attack.badRespawnPoint.link" to 0,
    "death.attack.badRespawnPoint.message" to 2,
    "death.attack.cactus" to 1,
    "death.attack.cactus.player" to 2,
    "death.attack.cramming" to 1,
    "death.attack.cramming.player" to 2,
    "death.attack.dragonBreath" to 1,
    "death.attack.dragonBreath.player" to 2,
    "death.attack.drown" to 1,
    "death.attack.drown.player" to 2,
    "death.attack.dryout" to 1,
    "death.attack.dryout.player" to 2,
    "death.attack.even_more_magic" to 1,
    "death.attack.explosion" to 1,
    "death.attack.explosion.player" to 2,
    "death.attack.explosion.player.item" to 3,
    "death.attack.fall" to 0,
    "death.attack.fall.player" to 2,
    "death.attack.fallingBlock" to 1,
    "death.attack.fallingBlock.player" to 2,
    "death.attack.fallingStalactite" to 1,
    "death.attack.fallingStalactite.player" to 2,
    "death.attack.fireball" to 2,
    "death.attack.fireball.item" to 3,
    "death.attack.fireworks" to 1,
    "death.attack.fireworks.item" to 3,
    "death.attack.fireworks.player" to 2,
    "death.attack.flyIntoWall" to 1,
    "death.attack.flyIntoWall.player" to 2,
    "death.attack.freeze" to 1,
    "death.attack.freeze.player" to 2,
    "death.attack.generic" to 1,
    "death.attack.generic.player" to 2,
    "death.attack.genericKill" to 1,
    "death.attack.genericKill.player" to 2,
    "death.attack.hotFloor" to 1,
    "death.attack.hotFloor.player" to 2,
    "death.attack.indirectMagic" to 2,
    "death.attack.indirectMagic.item" to 3,
    "death.attack.inFire" to 1,
    "death.attack.inFire.player" to 2,
    "death.attack.inWall" to 1,
    "death.attack.inWall.player" to 2,
    "death.attack.lava" to 1,
    "death.attack.lava.player" to 2,
    "death.attack.lightningBolt" to 1,
    "death.attack.lightningBolt.player" to 2,
    "death.attack.mace_smash" to 2,
    "death.attack.mace_smash.item" to 3,
    "death.attack.magic" to 1,
    "death.attack.magic.player" to 2,
    "death.attack.message_too_long" to 0,
    "death.attack.mob" to 2,
    "death.attack.mob.item" to 3,
    "death.attack.onFire" to 1,
    "death.attack.onFire.item" to 3,
    "death.attack.onFire.player" to 2,
    "death.attack.outOfWorld" to 1,
    "death.attack.outOfWorld.player" to 2,
    "death.attack.outsideBorder" to 1,
    "death.attack.outsideBorder.player" to 2,
    "death.attack.player" to 2,
    "death.attack.player.item" to 3,
    "death.attack.sonic_boom" to 1,
    "death.attack.sonic_boom.item" to 3,
    "death.attack.sonic_boom.player" to 2,
    "death.attack.stalagmite" to 1,
    "death.attack.stalagmite.player" to 2,
    "death.attack.starve" to 1,
    "death.attack.starve.player" to 2,
    "death.attack.sting" to 1,
    "death.attack.sting.item" to 3,
    "death.attack.sting.player" to 2,
    "death.attack.sweetBerryBush" to 1,
    "death.attack.sweetBerryBush.player" to 2,
    "death.attack.thorns" to 2,
    "death.attack.thorns.item" to 3,
    "death.attack.thrown" to 2,
    "death.attack.thrown.item" to 3,
    "death.attack.trident" to 2,
    "death.attack.trident.item" to 3,
    "death.attack.wither" to 1,
    "death.attack.wither.player" to 2,
    "death.attack.witherSkull" to 2,
    "death.attack.witherSkull.item" to 3,
    "death.fell.accident.generic" to 1,
    "death.fell.accident.ladder" to 1,
    "death.fell.accident.other_climbable" to 1,
    "death.fell.accident.scaffolding" to 1,
    "death.fell.accident.twisting_vines" to 1,
    "death.fell.accident.vines" to 1,
    "death.fell.accident.weeping_vines" to 1,
    "death.fell.assist" to 2,
    "death.fell.assist.item" to 3,
    "death.fell.finish" to 2,
    "death.fell.finish.item" to 3,
    "death.fell.killer" to 1,
    "deathScreen.quit.confirm" to 0,
    "deathScreen.respawn" to 0,
    "deathScreen.score" to 0,
    "deathScreen.score.value" to 0,
    "deathScreen.spectate" to 0,
    "deathScreen.title" to 0,
    "deathScreen.title.hardcore" to 0,
    "deathScreen.titleScreen" to 0
)

///summon minecraft:drowned ~ ~ ~ {equipment:{mainhand:{id:"minecraft:trident"}}}

class DeathInfoParser {

    //death.fell.accident.generic


    /*
    TranslatableComponentImpl{key="death.attack.arrow",
    arguments=[
        TranslationArgumentImpl{
        value=TextComponentImpl{
            content="kardelio", style=StyleImpl{obfuscated=not_set, bold=not_set, strikethrough=not_set, underlined=not_set, italic=not_set, color=null, shadowColor=null, clickEvent=ClickEvent{action=suggest_command, payload=TextImpl{value="/tell kardelio "}}, hoverEvent=HoverEvent{action=show_entity, value=ShowEntity{type=KeyImpl{namespace="minecraft", value="player"}, id=61c51939-5f67-458c-8385-ea13201d7799, name=TextComponentImpl{content="kardelio", style=StyleImpl{obfuscated=not_set, bold=not_set, strikethrough=not_set, underlined=not_set, italic=not_set, color=null, shadowColor=null, clickEvent=null, hoverEvent=null, insertion=null, font=null}, children=[]}}}, insertion="kardelio", font=null}, children=[]}
        },
        TranslationArgumentImpl{
            value=TranslatableComponentImpl{key="entity.minecraft.skeleton", arguments=[], fallback=null, style=StyleImpl{obfuscated=not_set, bold=not_set, strikethrough=not_set, underlined=not_set, italic=not_set, color=null, shadowColor=null, clickEvent=null, hoverEvent=HoverEvent{action=show_entity, value=ShowEntity{type=KeyImpl{namespace="minecraft", value="skeleton"}, id=074d3f8e-9b62-4fad-8781-3ded31464dce, name=TranslatableComponentImpl{key="entity.minecraft.skeleton", arguments=[], fallback=null, style=StyleImpl{obfuscated=not_set, bold=not_set, strikethrough=not_set, underlined=not_set, italic=not_set, color=null, shadowColor=null, clickEvent=null, hoverEvent=null, insertion=null, font=null}, children=[]}}}, insertion="074d3f8e-9b62-4fad-8781-3ded31464dce", font=null}, children=[]}
            }
            ], fallback=null, style=StyleImpl{obfuscated=not_set, bold=not_set, strikethrough=not_set, underlined=not_set, italic=not_set, color=null, shadowColor=null, clickEvent=null, hoverEvent=null, insertion=null, font=null}, children=[]}
>

 val translationKey = deathMessageComponent.key()
        val arguments = deathMessageComponent.arguments()

        // Handle the specific case for "death.attack.arrow.item"
        if (translationKey == "death.attack.arrow.item" && arguments.size >= 3) {
            val killedPlayerComponent = arguments[0]
            val killerComponent = arguments[1]
            val itemComponent = arguments[2]
     */

    /**
     * takes the death messages
     *
     *
     * FALL
     * death.fell.accident.generic
     *
     * args player name
     */
    fun convertToDeathType(deathMessageComponent: Component?): String {
        println(deathMessageComponent)
        if (deathMessageComponent is TranslatableComponent) {
            val key = deathMessageComponent.key().toString()
            val arguments = deathMessageComponent.arguments()
            // Get the killer name if the list has at least 2 arguments
            val playername = if (arguments.size >= 1) {
                when (val a = arguments[0].value()) {
                    // Handle a translatable component, which has a key
                    is TranslatableComponent -> {
                        a.key()
                    }
                    // Handle a simple text component, which has content
                    is TextComponent -> {
                        // You might want to handle this case differently, or just return the text
                        a.content()
                    }
                    // Handle any other component type
                    else -> {
                        "Unknown_Death_Type"
                    }
                }
            } else {
                ""
            }
            val killerName = if (arguments.size >= 2) {
                when (val a = arguments[1].value()) {
                    // Handle a translatable component, which has a key
                    is TranslatableComponent -> {
                        a.key()
                    }
                    // Handle a simple text component, which has content
                    is TextComponent -> {
                        // You might want to handle this case differently, or just return the text
                        a.content()
                    }
                    // Handle any other component type
                    else -> {
                        "Unknown_Death_Type"
                    }
                }
//                (arguments[1].value() as TranslatableComponent).key()
            } else {
                ""
            }

            // Get the item name if the list has at least 3 arguments
            val itemName = if (arguments.size >= 3) {
                when (val a = arguments[2].value()) {
                    // Handle a translatable component, which has a key
                    is TranslatableComponent -> {
                        a.key()
                    }
                    // Handle a simple text component, which has content
                    is TextComponent -> {
                        // You might want to handle this case differently, or just return the text
                        a.content()
                    }
                    // Handle any other component type
                    else -> {
                        "Unknown_Death_Type"
                    }
                }
            } else {
                ""
            }
            return "${key}: ${playername} ---------------- ${killerName} --------------- ${itemName}"
        }
        return ""
    }
}