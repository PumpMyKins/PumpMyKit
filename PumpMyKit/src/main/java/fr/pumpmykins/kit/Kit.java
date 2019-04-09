package fr.pumpmykins.kit;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;

public class Kit {

	private String name;
	
	private int x;
	private int y;
	private int z;
	
	private String last_update;
	private UUID last_updator;
	private UUID creator;
	
	static Kit createKit(UUID creator, BlockPos chest_location, String kitname) {
		
		Kit k = new Kit();
		
		k.setLast_updator(creator);
		k.setCreator(creator);
		
		k.setX(chest_location.getX());
		k.setY(chest_location.getY());
		k.setZ(chest_location.getZ());
		
		k.setName(kitname);
		
		return k;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String getLast_update() {
		return last_update;
	}

	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}

	public UUID getLast_updator() {
		return last_updator;
	}

	public void setLast_updator(UUID last_updator) {
		this.last_updator = last_updator;
	}

	public UUID getCreator() {
		return creator;
	}

	public void setCreator(UUID creator) {
		this.creator = creator;
	}
	
	
}