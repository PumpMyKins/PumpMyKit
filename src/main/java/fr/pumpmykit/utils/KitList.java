package fr.pumpmykit.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import fr.pumpmykit.MainKit;
import fr.pumpmykit.exceptions.DuplicateKitException;
import fr.pumpmykit.exceptions.InsufisentKitsToRandException;
import fr.pumpmykit.exceptions.UnfoudKitException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class KitList extends WorldSavedData {

	public static final String KEY = MainKit.MODID + "_kits";

	public static KitList getData(World w) {

		MapStorage storage = w.getMapStorage();
		KitList instance = (KitList) storage.getOrLoadData(KitList.class, KEY);
		if(instance == null) {
			instance = new KitList();
			storage.setData(KEY, instance);
		}
		return instance;
		
	}
	
	public KitList() {
		super(KEY);
		this.kitlist = new HashMap<>();
	}
	
	public KitList(String s) {
		super(s);
		this.kitlist = new HashMap<>();
	}


	private HashMap<String, Kit> kitlist;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		NBTTagList kit_list = nbt.getTagList(KEY, NBT.TAG_COMPOUND);
		
		for(int i =0; i < kit_list.tagCount(); i++) {

			NBTTagCompound tmp_nbt = kit_list.getCompoundTagAt(i);

			String name = tmp_nbt.getString("name");
			String displayname = tmp_nbt.getString("displayname");

			List<ItemStack> items = new ArrayList<>();
			NBTTagList contentList = (NBTTagList) tmp_nbt.getTag("items");			
			for(int y =0; y < contentList.tagCount(); y++) {

				NBTTagCompound tmp_nbt_item = contentList.getCompoundTagAt(y);
				items.add(new ItemStack(tmp_nbt_item));
				
			}
			
			Kit tmp_k = new Kit(name,displayname,items);
			this.kitlist.put(name, tmp_k);

		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		NBTTagList kit_list = new NBTTagList();
		for(Kit k : this.kitlist.values()) {

			NBTTagCompound tmp = new NBTTagCompound();
			tmp.setString("name", k.getName());
			tmp.setString("displayname", k.getDisplayName());
			
			NBTTagList contentList = new NBTTagList();
			for (ItemStack item : k.getItems()) {
				
				NBTTagCompound tag = item.serializeNBT();
				contentList.appendTag(tag);
				
			}
			
			tmp.setTag("items", contentList);
			
			kit_list.appendTag(tmp);
			
		}
		compound.setTag(KEY, kit_list);
		return compound;
	}

	public void removeKit(String kitname) throws UnfoudKitException {
		if(!this.kitlist.containsKey(kitname)) {
			throw new UnfoudKitException(kitname);
		}
		this.kitlist.remove(kitname);	
		markDirty();
	}

	public void addKit(Kit k) throws DuplicateKitException {
		if(this.kitlist.containsKey(k.getName())) {
			throw new DuplicateKitException(k.getName());
		}
		this.kitlist.put(k.getName(), k);	
		markDirty();
	}

	public Kit getKit(String kitname) throws UnfoudKitException {
		if(!this.kitlist.containsKey(kitname)) {
			throw new UnfoudKitException(kitname);
		}		
		return this.kitlist.get(kitname);		
	}
	
	public Kit getRandomKit() {
		
		Random random = new Random();
		return (Kit) this.kitlist.values().toArray()[random.nextInt(this.kitlist.size()-1)];
		
	}

	public HashMap<String,Kit> getKitlist() {
		return this.kitlist;
	}

}
