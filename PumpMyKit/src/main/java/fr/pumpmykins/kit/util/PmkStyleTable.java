package fr.pumpmykins.kit.util;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class PmkStyleTable {

	public static Style bold() {
		
		Style s = new Style();
		s.setBold(true);
		
		return s;
	}
	
	public static Style italic() {
		
		Style s = new Style();
		s.setItalic(true);
		
		return s;
	}
	
	public static Style orangeBold() {
		
		Style s = new Style();
		s.setBold(true);
		s.setColor(TextFormatting.GOLD);
		return s;
	}
	
	public static Style itemList() {
		
		Style s = new Style();
		s.setItalic(true);
		s.setColor(TextFormatting.BLUE);
		
		return s;
	}
	
	public static Style itemNumber() {
		
		Style s = new Style();
		s.setBold(true);
		s.setColor(TextFormatting.DARK_RED);
		
		return s;
	}
	
	public static Style kitSelect(String kitname) {
		
		Style s = new Style();
		s.setColor(TextFormatting.DARK_BLUE);
		s.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("/kitselect "+kitname)));
		s.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/kitselect "+kitname));
		
		return s;
	}
}
