package doc.dynamictanks.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import doc.dynamictanks.Utils.StringUtils;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

public class TankCoreBlockItem extends ItemBlock {

	TileEntityMultiTankCore core = new TileEntityMultiTankCore();
	
	public TankCoreBlockItem(int par1)
	{
		super(par1);
	}

	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4)
	{
		if (itemstack.stackTagCompound != null) {
			core.tank.readFromNBT(itemstack.stackTagCompound);
			
			list.add(StringUtils.Cap(core.tank.getFluid().getFluid().getName()));
			list.add(StringUtils.parseCommas(core.tank.getFluidAmount() + "", "", " mB"));
		}
	}

}
