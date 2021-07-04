package plugin.spigot.defaulpackage;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
		ItemStackComparator l_Comparer = new ItemStackComparator();
		
		// Check if stacks have same item
		if(l_Comparer.compare(i1, i2) == 0) {
			int l_CurrentAmount = i1.getAmount();
			int l_CurrentMaxSize = i1.getMaxStackSize();
			
			// Check if the first stack is not full
			if (l_CurrentAmount < l_CurrentMaxSize) {
				int l_CurrentRemainingAmount = i1.getMaxStackSize() - i1.getAmount();
				int l_NextAmount = i2.getAmount();
				
				// Compact full second stack, else compact only the possible quantity
				if (l_NextAmount <= l_CurrentRemainingAmount) {
					i1.setAmount(l_CurrentAmount + l_NextAmount);
					i2.setType(Material.AIR);
				} else {
					i1.setAmount(l_CurrentAmount + l_CurrentRemainingAmount);
					i2.setAmount(l_NextAmount - l_CurrentRemainingAmount);
				}
				
				return true;
			}
		}
		
		return false;
	}
}
