package doc.dynamictanks.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Chipset extends Item {
	
	public static String[] names = { "Coal", "Iron", "Gold", "Lapis", "Diamond", "Emerald", "Starry", "Auto-Output" };
	public static float[] multi = { 1.05f, 1.25f, 1.50f, 1.75f, 2.00f, 2.15f, 2.50f, 1.00f };
	public static String[] multiStr = { "1.05", "1.25", "1.50", "1.75", "2.00", "2.15", "2.50", "Output Liquids" };
	Icon[] icons = new Icon[8]; //coal, iron, gold, lapis, diamond, emerald, starry;
	
	public Chipset(int id) {
		super(id);
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		list.add("x" + multiStr[stack.getItemDamage()]);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return (names[stack.getItemDamage()] + " Chipset");
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int itemId, CreativeTabs tab, List list) {
		for (int i = 0; i < names.length; i++) {
			list.add(new ItemStack(itemId, 1, i));
		}
	}
	
	@Override
	public void registerIcons(IconRegister register) {
		icons[0] = register.registerIcon("dynamictanks:coal_chip");
		icons[1] = register.registerIcon("dynamictanks:iron_chip");
		icons[2] = register.registerIcon("dynamictanks:gold_chip");
		icons[3] = register.registerIcon("dynamictanks:lapis_chip");
		icons[4] = register.registerIcon("dynamictanks:diamond_chip");
		icons[5] = register.registerIcon("dynamictanks:emerald_chip");
		icons[6] = register.registerIcon("dynamictanks:star_chip");
		icons[7] = register.registerIcon("dynamictanks:star_chip");
	}
	
	@Override
	public Icon getIconFromDamage(int i) {
		return icons[i];
	}
	
}
