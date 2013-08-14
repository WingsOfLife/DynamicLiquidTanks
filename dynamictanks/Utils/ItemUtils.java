package doc.dynamictanks.Utils;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayer;
import doc.dynamictanks.block.BlockManager;
import doc.dynamictanks.items.Chipset;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

public class ItemUtils {

	public static ItemStack removeSingleItem (ItemStack is)
	{
		is.splitStack(1);
		return is;
	}

	public static float chipsetMultiplier(int index) {
		if (index < Chipset.multi.length) {
			return Chipset.multi[index];
		}
		return 1.00f;
	}

	public static int returnMeta(float value) {
		if (value == 1.05f)
			return 0;
		else if (value == 1.25f)
			return 1;
		else if (value == 1.50f)
			return 2;
		else if (value == 1.75f)
			return 3;
		else if (value == 2.00f)
			return 4;
		else if (value == 2.15f)
			return 5;
		else if (value == 2.50f)
			return 6;
		return -1;
	}

	public static void dropItem(int itemID, int meta, World world, int x, int y, int z) {
		EntityItem el = new EntityItem(world, x, y, z, new ItemStack(itemID, 1, meta));
		world.spawnEntityInWorld(el);
	}

	public static ItemStack consumeItem (ItemStack stack)
	{
		if (stack.stackSize == 1)
		{
			if (stack.getItem().hasContainerItem())
				return stack.getItem().getContainerItemStack(stack);
			else
				return null;
		}
		else
		{
			stack.splitStack(1);
			return stack;
		}
	}

	public static boolean removeBlockByPlayer(World world, int x, int y, int z)
	{
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			ItemStack itemStack = new ItemStack(BlockManager.tankCore);
			TileEntityMultiTankCore tank = (TileEntityMultiTankCore) world.getBlockTileEntity(x, y, z);
			if (tank != null) {
				if (tank.tank.getFluid() != null)
					itemStack.setItemDamage(Math.round(tank.tank.getFluidAmount() * 16));
				NBTTagCompound nbt = new NBTTagCompound();
				tank.tank.writeToNBT(nbt);
				itemStack.setTagCompound(nbt);
			}
			float f = 0.7F;
			double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, itemStack);
			entityitem.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(entityitem);
		}
		return world.setBlockToAir(x, y, z);
	}
	
	public static ItemStack dropItemStack(World world, int x, int y, int z)
	{
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			ItemStack itemStack = new ItemStack(BlockManager.tankCore);
			TileEntityMultiTankCore tank = (TileEntityMultiTankCore) world.getBlockTileEntity(x, y, z);
			if (tank != null) {
				itemStack.setItemDamage(Math.round(tank.tank.getFluidAmount() * 16));
				System.out.println(tank.tank.getFluidAmount());
				NBTTagCompound nbt = new NBTTagCompound();
				tank.tank.writeToNBT(nbt);
				itemStack.setTagCompound(nbt);
			}
			return itemStack;
		}
		return null;
	}

}
