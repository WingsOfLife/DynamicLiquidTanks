package doc.dynamictanks.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doc.dynamictanks.DynamicTanks;
import doc.dynamictanks.Utils.ItemUtils;
import doc.dynamictanks.client.ClientProxy;
import doc.dynamictanks.common.ModConfig;
import doc.dynamictanks.tileentity.TileEntityMulitTankDimExt;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

public class BlockTankDimExt extends BlockContainer {

	Icon sides, topBottom;

	public BlockTankDimExt(int id) {
		super(id, Material.iron);
		this.setUnlocalizedName("Dimensional Extension");
		setBlockBounds(0.125f, 0f, 0.125f, 0.875f, 0.875f, 0.875f);
		setHardness(1.5f);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}

	@Override
	public int getRenderType()
	{
		return ClientProxy.multiTankRender;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderBlockPass()	{
		return 1;
	}

	@Override
	public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float clickX, float clickY, float clickZ)
	{

		ItemStack heldItem = player.inventory.getCurrentItem();
		TileEntityMulitTankDimExt getLocations = (TileEntityMulitTankDimExt) world.getBlockTileEntity(x, y, z);
		
		if (heldItem != null && heldItem.itemID == Item.dyePowder.itemID) {
			getLocations.dyeIndex = heldItem.getItemDamage();
			world.markBlockForRenderUpdate(x, y, z);
			return true;
		}
		
		if (!FluidContainerRegistry.isBucket(heldItem)) {
			player.openGui(DynamicTanks.instance, ModConfig.GUIIDs.DimExt, world, x, y, z);
			return true;
		}
		
		/*if (world.isRemote)
			return false;*/
		
		if (heldItem != null /*&& !world.isRemote*/ && getLocations.isActive)
		{
			FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(player.getCurrentEquippedItem());
			TileEntityMultiTankCore logic = ((TileEntityMulitTankDimExt) world.getBlockTileEntity(x, y, z)).getCore();
			
			if (liquid != null && logic != null)
			{
				int amount = logic.fill(ForgeDirection.UNKNOWN, liquid, false);
				if (amount == liquid.amount)
				{
					logic.fill(ForgeDirection.UNKNOWN, liquid, true);
					if (!player.capabilities.isCreativeMode)
						player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemUtils.consumeItem(heldItem));
					return true;
				}
				else
					return true;
			}
			else if (FluidContainerRegistry.isBucket(heldItem) && logic != null)
			{
				FluidStack fillLiquid = logic.tank.getFluid();
				ItemStack fillStack = FluidContainerRegistry.fillFluidContainer(fillLiquid, heldItem);
				if (fillStack != null)
				{
					logic.drain(ForgeDirection.UNKNOWN, FluidContainerRegistry.getFluidForFilledItem(fillStack).amount, true);
					if (!player.capabilities.isCreativeMode)
					{
						if (heldItem.stackSize == 1)
						{
							player.inventory.setInventorySlotContents(player.inventory.currentItem, fillStack);
						}
						else
						{
							player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemUtils.consumeItem(heldItem));

							if (!player.inventory.addItemStackToInventory(fillStack))
							{
								player.dropPlayerItem(fillStack);
							}
						}
					}
					return true;
				}
				else
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public final int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		TileEntity getTextures = world.getBlockTileEntity(x, y, z);
		if (getTextures instanceof TileEntityMulitTankDimExt) {
			TileEntityMulitTankDimExt casted = ((TileEntityMulitTankDimExt) getTextures);
			if (casted.dyeIndex != -1) {
				return ItemDye.dyeColors[casted.dyeIndex];
			}
		}
		return super.colorMultiplier(world, x, y, z);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMulitTankDimExt();
	}

	@Override
	public Icon getIcon(int var1, int var2) {
		switch(var1) {
		case 0:
		case 1: return topBottom;
		default: return sides;
		}
	}

	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		switch(side) {
		case 0: 
		case 1: return topBottom;
		default: return sides;
		}
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		sides = iconRegister.registerIcon("dynamictanks:dimExtTank");
		topBottom = iconRegister.registerIcon("dynamictanks:dinExtTank_Top");
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world,
						x + rx, y + ry, z + rz,
						new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}

}
