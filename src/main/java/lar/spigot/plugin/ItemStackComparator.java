package lar.spigot.plugin;

import java.util.Comparator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lar.spigot.plugin.entities.SortingType;

public class ItemStackComparator implements Comparator<ItemStack> {

	private SortingType sortingType;
	
	public ItemStackComparator() {
		this.sortingType = SortingType.SIMPLE_ASC;
	}
	
	
	public ItemStackComparator(SortingType sortingType) {
		this.sortingType = sortingType;
	}
	
	public void setSortingType(SortingType sortingType) {
		this.sortingType = sortingType;
	}
	
	
	@Override
	public int compare(ItemStack i1, ItemStack i2) {
		switch(sortingType) {
		case SIMPLE_ASC:
			if (i1.getType().equals(Material.AIR)) return 1;
	    	if (i2.getType().equals(Material.AIR)) return -1;	
	        return i1.getType().compareTo(i2.getType());
		case SIMPLE_DESC:
			if (i1.getType().equals(Material.AIR)) return 1;
	    	if (i2.getType().equals(Material.AIR)) return -1;
	        return -1 * i1.getType().compareTo(i2.getType());
		default: 
			if (i1.getType().equals(Material.AIR)) return 1;
			if (i2.getType().equals(Material.AIR)) return -1;
			return i1.getType().compareTo(i2.getType());
		}
		
	}

}
