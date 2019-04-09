package fr.pumpmykins.kit;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class KitList extends WorldSavedData {
		
	public KitList(String key) {
		super(key);
		this.kitlist = new ArrayList<Kit>();
	}
	public KitList() {
		
		super(MainKit.getKitlistKey());
		this.kitlist = new ArrayList<Kit>();
	}
	
	
	private List<Kit> kitlist;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		NBTTagList kit_list = nbt.getTagList(MainKit.getKitlistKey(), NBT.TAG_COMPOUND);
		for(int i =0; i < kit_list.tagCount(); i++) {
			
			NBTTagCompound tmp_nbt = kit_list.getCompoundTagAt(i);
			Kit tmp_k = new Kit();
			
			tmp_k.setName(tmp_nbt.getString("name"));
			
			tmp_k.setCreator(tmp_nbt.getUniqueId("creator"));
			
			tmp_k.setLast_updator(tmp_nbt.getUniqueId("last_updator"));
			tmp_k.setLast_update(tmp_nbt.getString("last_update"));
			
			tmp_k.setX(tmp_nbt.getInteger("x"));
			tmp_k.setY(tmp_nbt.getInteger("y"));
			tmp_k.setZ(tmp_nbt.getInteger("z"));
			
			this.kitlist.add(tmp_k);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		NBTTagList kit_list = new NBTTagList();
		for(Kit k : this.kitlist) {
			
			NBTTagCompound tmp = new NBTTagCompound();
			tmp.setString("name", k.getName());
			tmp.setUniqueId("creator", k.getCreator());
			tmp.setUniqueId("last_updator", k.getLast_updator());
			tmp.setString("last_update", k.getLast_update());
			tmp.setInteger("x", k.getX());
			tmp.setInteger("y", k.getY());
			tmp.setInteger("z", k.getZ());
		
			kit_list.appendTag(tmp);
		}
		compound.setTag(MainKit.getKitlistKey(), kit_list);
		return null;
	}
	
	public void removeKit(Kit k) {
		
		for(int i = 0; i < this.kitlist.size(); i ++) {
			if(this.kitlist.get(i).getName().equals(k.getName())) {
				
				kitlist.remove(i);
				markDirty();
				break;
			}
		}
	}
	
	public void removeKit(String name) {
		
		for(int i = 0; i < this.kitlist.size(); i ++) {
			if(this.kitlist.get(i).getName().equals(name)) {
				
				this.kitlist.remove(i);
			}
		}
	}
	
	public void addKit(Kit k) {
		
		boolean exist = false;
		for(int i = 0; i < this.kitlist.size(); i ++) {
			if(this.kitlist.get(i).getName().equals(k.getName())) {
				
				exist = true;
				break;
			}
		}
		if(!exist) {
			
			this.kitlist.add(k);
			markDirty();
		}
	}
	
	public Kit getKit(String kitname) {
		
		for(int i = 0; i < this.kitlist.size(); i ++) {
			if(this.kitlist.get(i).getName().equals(kitname)) {
				
				return this.kitlist.get(i);
			}
		}
		return null;
	}
	
	public Kit getKit(BlockPos pos) {
		
		for(int i = 0; i< this.kitlist.size(); i++) {
			
			Kit k = this.kitlist.get(i);
			if(pos.getX() == k.getX() && pos.getY() == k.getY() && pos.getZ() == k.getZ()) {
				
				return k;
			}
		}
		return null;
	}
	
	public List<Kit> getKitlist() {
		return this.kitlist;
	}
	
	public void markDirty() {
		
		this.setDirty(true);
	}
	
}
