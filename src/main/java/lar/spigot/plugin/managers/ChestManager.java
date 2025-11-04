package lar.spigot.plugin.managers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lar.spigot.plugin.ItemStackComparator;

public class ChestManager {

	/**
	* Compact the second item stack on the first item stack
	* This happen only if stacks have the same material and the first stack has enough space
	*
	* @param  i1  first item stack
	* @param  i2  second item stack
	* @return      true if stack has been compacted
	*/
	public static boolean CompactStack(ItemStack i1, ItemStack i2) {
		// Null & air check
		if (i1 == null || i2 == null || i1.getType() == Material.AIR || i2.getType() == Material.AIR)
			return false;

		// Non stackabili
		if (i1.getMaxStackSize() <= 1)
			return false;

		ItemStackComparator comparer = new ItemStackComparator();

		// Same item (not filled map)
		if (comparer.compare(i1, i2) == 0
				&& i1.getType() != Material.FILLED_MAP
				&& i2.getType() != Material.FILLED_MAP) {

			int currentAmount = i1.getAmount();
			int maxStackSize = i1.getMaxStackSize();

			if (currentAmount < maxStackSize) {
				int remaining = maxStackSize - currentAmount;
				int nextAmount = i2.getAmount();

				if (nextAmount <= remaining) {
					i1.setAmount(currentAmount + nextAmount);
					i2.setType(Material.AIR);
				} else {
					i1.setAmount(currentAmount + remaining);
					i2.setAmount(nextAmount - remaining);
				}
				return true;
			}
		}
		return false;
	}

}
