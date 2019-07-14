package fr.pumpmykit.utils;

import java.util.List;
import net.minecraft.item.ItemStack;

public class Kit {

	private String name;
	private String displayName;
	private List<ItemStack> items;
	
	public Kit(String name, String displayName, List<ItemStack> items) {
		super();
		this.name = name;
		this.displayName = displayName;
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void setItems(List<ItemStack> items) {
		this.items = items;
	}
	
}