package bk.minecraftplugin.playerWatcherPaperMCPlugin.lottery

import bk.minecraftplugin.playerWatcherPaperMCPlugin.utils.Random
import bk.minecraftplugin.playerWatcherPaperMCPlugin.utils.RandomImpl
import org.bukkit.inventory.ItemStack

/**
 * Wheel needs
 * - slots fixed
 * - items that take up x percent of slots?
 */

class LotteryWheel(val rng: Random = RandomImpl()) {
    private val items: MutableList<LotteryItem> = ArrayList<LotteryItem>()

    //    private val random: Random = Random()
    private lateinit var wheel: LotteryWheel

    fun getInstance(): LotteryWheel {
        if (this::wheel.isInitialized) {
            return this.wheel
        } else {
            this.wheel = LotteryWheel()
            return this.wheel
        }
    }

//    fun LotteryWheel() {
    // You can populate the wheel here or via a method.
    // For example, adding some items with different rarities.
    // For a more dynamic approach, you would load these from a config file.

    // You can add items with different rarities.
    // The total rarity doesn't need to be 1.0; the code below handles that.
    // Example:
    // addItem(new ItemStack(Material.DIAMOND, 1), 0.05);  // 5% chance
    // addItem(new ItemStack(Material.IRON_INGOT, 1), 0.2); // 20% chance
//    }

    fun addItem(item: ItemStack, rarity: Double) {
        if (rarity > 0) {
            items.add(LotteryItem(item, rarity))
        }
    }

    /**
     * Spins the wheel and returns a random item based on rarity.
     * @return The randomly selected ItemStack, or null if the wheel is empty.
     */
    fun spin(): ItemStack {
        if (items.isEmpty()) {
            throw Exception("OMG")
        }

        // Calculate the total weight of all items.
        var totalWeight = 0.0
        for (item in items) {
            totalWeight += item.rarity
        }

        // Generate a random number between 0 and the total weight. (1 if all percents add up to 100)
        val randomNumber: Double = rng.genDouble() * totalWeight

        /**
         * ALWAYS A NUMBER FORM 0 to 1
         */

        var currentWeight = 0.0
        for (item in items) {
            currentWeight += item.rarity
            if (randomNumber <= currentWeight) {
                return item.item.clone()
            }
        }
        throw Exception("dasdad")
    }
}
