package doc.dynamictanks.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LinkCard extends Item {

	public LinkCard(int par1) {
		super(par1);
		setCreativeTab(CreativeTabs.tabMisc);
		maxStackSize = 1;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		if (stack.stackTagCompound != null) {
			list.add("X: " + stack.stackTagCompound.getInteger("X") + " Y: " + stack.stackTagCompound.getInteger("Y") + " Z: " + stack.stackTagCompound.getInteger("Z"));
		} else {
			list.add("X: -- Y: -- Z: --");
		}
	}
	
	@Override
	public void registerIcons(IconRegister register) {
		this.itemIcon = register.registerIcon("dynamictanks:linkCard");
	}

}
