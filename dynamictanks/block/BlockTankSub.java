package doc.dynamictanks.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
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
import doc.dynamictanks.Utils.CTUtils;
import doc.dynamictanks.Utils.ItemUtils;
import doc.dynamictanks.Utils.PacketUtil;
import doc.dynamictanks.Utils.TEUtil;
import doc.dynamictanks.client.ClientProxy;
import doc.dynamictanks.client.KeyBinderCamo;
import doc.dynamictanks.common.ModConfig;
import doc.dynamictanks.items.ItemManager;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;
import doc.dynamictanks.tileentity.TileEntityMultiTankSub;

public class BlockTankSub extends BlockContainer {

	public BlockTankSub(int i)
	{
		super(i, Material.rock);
		this.setUnlocalizedName("Multi-Tank Extension");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setTickRandomly(true);
		setHardness(1.5F);
		setResistance(10.0F);
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
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
	public boolean canRenderInPass(int pass)
	{
		ClientProxy.renderPass = pass;
		return true;
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMultiTankSub();
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)	{
		super.onBlockAdded(world, x, y, z);
		TEUtil.setCoreBNCore(world, x, y, z);
		TEUtil.setCoreBNSub(world, x, y, z);
		if (((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).getCore() != null && ((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).getCore() != null) {
			((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).getCore().connectingTanks++;
			resizeTank(((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).getCore());
		}
	}

	@Override
	public void breakBlock(World par1World, int x, int y, int z, int par5, int par6) {
		//Block block = ((TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z)).side0 == -1 ? Block.blocksList[this.blockID] : Block.blocksList[((TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z)).side0];
		if (((TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z)).getCore() != null) {
			TileEntityMultiTankCore myCore = ((TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z)).getCore();
			//myCore.totalTankHardness = myCore.totalTankHardness - (block.blockHardness);// - 1.5);
			myCore.connectingTanks--;
			myCore.updateMe(par1World);
			resizeTank(((TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z)).getCore());
		}		
		//dropBlock(par1World, x, y, z, block.blockID, ((TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z)).meta0);
		super.breakBlock(par1World, x, y, z, par5, par6);
	}

	@Override
	public void onBlockClicked(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer) {
		ItemStack heldItem = par5EntityPlayer.inventory.getCurrentItem();
		TileEntityMultiTankSub getLocations = (TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z);
		
		if (par5EntityPlayer.isSneaking() && heldItem != null && Block.blocksList[heldItem.itemID] != null && Block.blocksList[heldItem.itemID].getBlockTextureFromSide(1) != null) {
			getLocations.side1 = heldItem.itemID;
			getLocations.meta1 = heldItem.getItemDamage();
			PacketUtil.sendPacketWithInt(PacketUtil.camo, heldItem.itemID, x, y, z);
			PacketUtil.sendPacketWithInt(PacketUtil.meta, heldItem.getItemDamage(), x, y, z);
			//getLocations.side1 = heldItem.itemID; getLocations.meta1 = heldItem.getItemDamage(); 
			par1World.markBlockForUpdate(x, y, z);
		} else if (par5EntityPlayer.isSneaking() && heldItem == null) {
			getLocations.side1 = -1;
			getLocations.meta1 = 0;
			PacketUtil.sendPacketWithInt(PacketUtil.camo, -1, x, y, z);
			PacketUtil.sendPacketWithInt(PacketUtil.meta, 0, x, y, z);
			par1World.markBlockForUpdate(x, y, z);
		}
	}
	
	@Override
	public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float clickX, float clickY, float clickZ)
	{
		ItemStack heldItem = player.inventory.getCurrentItem();
		TileEntityMultiTankSub getLocations = (TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z);
		
		if (heldItem != null && (heldItem.itemID == this.blockID || heldItem.itemID == BlockManager.tankCore.blockID))
			return false;
		
		if (heldItem != null && heldItem.itemID == Item.dyePowder.itemID) {
			getLocations.dyeIndex = heldItem.getItemDamage();
			world.markBlockForRenderUpdate(x, y, z);
		} 
		else if (heldItem != null && heldItem.itemID == Item.potion.itemID) {
			FluidStack liquid = new FluidStack(DynamicTanks.fluidPotion, FluidContainerRegistry.BUCKET_VOLUME);
			
			int amount = getLocations.getCore().fill(ForgeDirection.UNKNOWN, liquid, false);
			if (amount == liquid.amount && (getLocations.getCore().storedPotionId == -1 || getLocations.getCore().storedPotionId == heldItem.getItemDamage())) {
				getLocations.getCore().fill(ForgeDirection.UNKNOWN, liquid, true);
				ItemUtils.removeSingleItem(heldItem);
				player.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle, 1));
				getLocations.getCore().storedPotionId = heldItem.getItemDamage();
				return true;
			}
		}
		else if (heldItem != null && heldItem.itemID == Item.glassBottle.itemID && getLocations.getCore().storedPotionId != -1 && getLocations.getCore().tank.getFluid() != null) {
			FluidStack fillLiquid = getLocations.getCore().tank.getFluid();
			getLocations.getCore().drain(ForgeDirection.UNKNOWN, FluidContainerRegistry.BUCKET_VOLUME, true);
			ItemUtils.removeSingleItem(heldItem);
			player.inventory.addItemStackToInventory(new ItemStack(Item.potion, 1, getLocations.getCore().storedPotionId));
			
		}
		else if (heldItem != null && heldItem.itemID == ItemManager.chipSet.itemID && getLocations.getCore() != null) {
			if (getLocations.getCore().scalarMultiplier != 1.00f) {
				if (!world.isRemote) ItemUtils.dropItem(ItemManager.chipSet.itemID, ItemUtils.returnMeta(getLocations.getCore().scalarMultiplier), world, x, y, z);
			}
			
			getLocations.getCore().scalarMultiplier = ItemUtils.chipsetMultiplier(heldItem.getItemDamage());
			PacketUtil.sendPacketWithInt(PacketUtil.scalar, ItemUtils.chipsetMultiplier(heldItem.getItemDamage()), x, y, z);
			
			getLocations.getCore().setInventorySlotContents(1, new ItemStack(ItemManager.chipSet, 1, ItemUtils.returnMeta(getLocations.getCore().scalarMultiplier)));
			
			ItemUtils.removeSingleItem(heldItem);
			
			resizeTank(getLocations.getCore());
			world.markBlockForUpdate(x, y, z);
		}	
		else if (heldItem == null || !FluidContainerRegistry.isBucket(heldItem) && getLocations.getCore() != null && heldItem.itemID != Item.dyePowder.itemID) {
			player.openGui(DynamicTanks.instance, ModConfig.GUIIDs.tankCE, world, x, y, z);
			return true;
		} 
		else if (heldItem != null)
		{
			FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(player.getCurrentEquippedItem());
			TileEntityMultiTankCore logic = ((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).getCore();
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
				//ILiquidTank[] tanks = logic.getTanks(ForgeDirection.UNKNOWN);
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
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int dir)
	{
		TileEntity getTextures = world.getBlockTileEntity(x, y, z);
		if (getTextures instanceof TileEntityMultiTankSub) {
			try {
				TileEntityMultiTankSub casted = ((TileEntityMultiTankSub) getTextures);
				switch (dir) {
				case 0: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return CTUtils.getBlocksAroundToBo(world, x, y, z, dir, this.blockID);
				case 1: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return CTUtils.getBlocksAroundToBo(world, x, y, z, dir, this.blockID);
				case 2: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return CTUtils.getBlocksAroundNoSo(world, x, y, z, dir, this.blockID);
				case 3: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return CTUtils.getBlocksAroundNoSo(world, x, y, z, dir, this.blockID);
				case 4: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return CTUtils.getBlocksAroundEaWe(world, x, y, z, dir, this.blockID);
				case 5: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return CTUtils.getBlocksAroundEaWe(world, x, y, z, dir, this.blockID);
				}
			} catch (Exception e) {
				return blockIcon;
			}
		}
		return blockIcon;
	}
	

	@Override
	@SideOnly(value = Side.CLIENT)
	public final int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		TileEntity getTextures = world.getBlockTileEntity(x, y, z);
		if (getTextures instanceof TileEntityMultiTankSub) {
			TileEntityMultiTankSub casted = ((TileEntityMultiTankSub) getTextures);
			if (casted.dyeIndex != -1) {
				return ItemDye.dyeColors[casted.dyeIndex];
			} else {
				try {
					return Block.blocksList[casted.side1].colorMultiplier(world, x, y, z);
				} catch (Throwable t) {
					return super.colorMultiplier(world, x, y, z);
				}
			}
		}
		return super.colorMultiplier(world, x, y, z);
	}

	@Override
	public boolean shouldSideBeRendered (IBlockAccess world, int x, int y, int z, int side)
	{
		int bID = world.getBlockId(x, y, z);
		return (bID == this.blockID || bID == BlockManager.tankCore.blockID) /*&& shouldSideRender(world, x, y, z)*/ ? false : super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	public int getLightValue (IBlockAccess world, int x, int y, int z)
	{
		TileEntity logic = world.getBlockTileEntity(x, y, z);
		if (logic != null && logic instanceof TileEntityMultiTankSub)
			if (((TileEntityMultiTankSub) logic).getCore() != null)
				return ((TileEntityMultiTankSub) logic).getCore().getBrightness();
		return 0;
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		CTUtils.leftRight = iconRegister.registerIcon("dynamictanks:leftRight");
		CTUtils.upAndDown = iconRegister.registerIcon("dynamictanks:topBottom");
		CTUtils.left = iconRegister.registerIcon("dynamictanks:left");
		CTUtils.right = iconRegister.registerIcon("dynamictanks:right");
		CTUtils.top = iconRegister.registerIcon("dynamictanks:top");
		CTUtils.bottom = iconRegister.registerIcon("dynamictanks:bottom");
		CTUtils.leftBottom = iconRegister.registerIcon("dynamictanks:leftBottom");
		CTUtils.leftTop = iconRegister.registerIcon("dynamictanks:leftTop");
		CTUtils.rightBottom = iconRegister.registerIcon("dynamictanks:rightBottom");
		CTUtils.rightTop = iconRegister.registerIcon("dynamictanks:rightTop");
		CTUtils.onlyTop = iconRegister.registerIcon("dynamictanks:onlyTop");
		CTUtils.onlyBottom = iconRegister.registerIcon("dynamictanks:onlyBottom");
		CTUtils.onlyRight = iconRegister.registerIcon("dynamictanks:onlyRight");
		CTUtils.onlyLeft = iconRegister.registerIcon("dynamictanks:onlyLeft");
		CTUtils.empty = iconRegister.registerIcon("dynamictanks:empty");
		this.blockIcon = iconRegister.registerIcon("dynamictanks:single");
	}

	public void updateTick(World par1World, int x, int y, int z, Random rand) {
		/*par1World.markBlockForUpdate(x, y, z);
		if (((TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z)).getCore() == null) {
			TEUtil.setCoreBNCore(par1World, x, y, z);
			TEUtil.setCoreBNSub(par1World, x, y, z);
			if (((TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z)).getCore() != null) {
				((TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z)).getCore().connectingTanks++;
				resizeTank(((TileEntityMultiTankSub) par1World.getBlockTileEntity(x, y, z)).getCore());
				par1World.markBlockForUpdate(x, y, z);
			}
			par1World.markBlockForUpdate(x, y, z);
		} */
	}

	@Override
	public int tickRate(World par1World)
	{
		return 2;
	}

	public void resizeTank(TileEntityMultiTankCore toResize) {
		if (toResize != null) {
			int calculateSize = (int) (((10 * toResize.connectingTanks) * (toResize.connectingTanks * .195)) * toResize.scalarMultiplier);//+ (int)toResize.totalTankHardness;
			if (toResize != null && toResize.containsLiquid()) {
				FluidStack liquid = toResize.tank.getFluid();
				int amount = toResize.tank.getFluid().amount;// > calculateSize * LiquidContainerRegistry.BUCKET_VOLUME ? calculateSize : toResize.tank.getLiquid().amount; //Check here
				toResize.tank.setCapacity(calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME); //= new FluidTank(liquid.getFluid(), amount, calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME);
			} else if (toResize != null) {
				toResize.tank.setCapacity(calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME); //= new FluidTank(calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME);
			}
		}
	}

	/*public boolean shouldSideRender(IBlockAccess world, int x, int y, int z) {
		TileEntityMultiTankSub check = (TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z);
		return check.side0 == -1 && check.side1 == -1 && check.side2 == -1 && check.side3 == -1 && check.side4 == -1 && check.side5 == -1 ? true : false;
	}*/
	//TODO



	/*public void dropBlock(World world, int x, int y, int z, int toDropID, int meta) {
		if (toDropID != this.blockID) {
			EntityItem toBeDropped = new EntityItem(world, x, y, z, new ItemStack(toDropID, 1, meta));
			world.spawnEntityInWorld(toBeDropped);
		}
	}*/

}
