package doc.dynamictanks.block;

import java.util.ArrayList;

import doc.dynamictanks.tileentity.TileEntityMultiTankSub;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doc.dynamictanks.DynamicTanks;
import doc.dynamictanks.Utils.CTUtils;
import doc.dynamictanks.Utils.ItemUtils;
import doc.dynamictanks.Utils.PacketUtil;
import doc.dynamictanks.Utils.TEUtil;
import doc.dynamictanks.Utils.collisionEffects;
import doc.dynamictanks.client.ClientProxy;
import doc.dynamictanks.common.ModConfig;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

public class BlockTankCore extends BlockContainer {

	public BlockTankCore(int i)
	{
		super(i, Material.rock);
		this.setUnlocalizedName("Multi-Tank Core");
		this.setCreativeTab(CreativeTabs.tabBlock);
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
	public int getRenderBlockPass()
	{
		return 1;
	}

	@Override
	public boolean canRenderInPass(int pass)
	{
		ClientProxy.renderPass = pass;
		return true;
	}

	@Override
	public int tickRate(World par1World)
	{
		return 10;
	}

	@Override
	public boolean hasTileEntity(int meta) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMultiTankCore();
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("dynamictanks:single");
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
		return true;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int dir)
	{
		TileEntity getTextures = world.getBlockTileEntity(x, y, z);
		if (getTextures instanceof TileEntityMultiTankCore) {
			try {
				TileEntityMultiTankCore casted = ((TileEntityMultiTankCore) getTextures);
				switch (dir) {
				case 0: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return this.blockIcon;
				case 1: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return this.blockIcon;
				case 2: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return this.blockIcon;
				case 3: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return this.blockIcon;
				case 4: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return this.blockIcon;
				case 5: if (casted.side1 != -1) return CTUtils.blockIcon(casted.side1, dir, casted.meta1); else return this.blockIcon;
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
		if (getTextures instanceof TileEntityMultiTankCore) {
			TileEntityMultiTankCore casted = ((TileEntityMultiTankCore) getTextures);
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
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		TileEntityMultiTankCore core = (TileEntityMultiTankCore) par1World.getBlockTileEntity(par2, par3, par4);
		if (core.containsLiquid()) ItemUtils.removeBlockByPlayer(par1World, par2, par3, par4);
		else ItemUtils.dropItem(BlockManager.tankCore.blockID, 0, par1World, par2, par3, par4);
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.clear();
		return ret;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		if (par6ItemStack.stackTagCompound == null) 
			return;

		TileEntityMultiTankCore setInfo = (TileEntityMultiTankCore) par1World.getBlockTileEntity(par2, par3, par4);
		setInfo.initalizeDelay = 0;
		setInfo.tank.readFromNBT(par6ItemStack.stackTagCompound);

	}

	@Override
    @SideOnly(Side.CLIENT)
	public void onBlockClicked(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer) {
		ItemStack heldItem = par5EntityPlayer.inventory.getCurrentItem();
		TileEntityMultiTankCore getLocations = (TileEntityMultiTankCore) par1World.getBlockTileEntity(x, y, z);

		if (par5EntityPlayer.isSneaking() && heldItem != null && heldItem.itemID < 4096 && Block.blocksList[heldItem.itemID] != null && Block.blocksList[heldItem.itemID].getBlockTextureFromSide(1) != null) {
			getLocations.side1 = heldItem.itemID;
			getLocations.meta1 = heldItem.getItemDamage();
			PacketUtil.sendPacketWithInt(PacketUtil.camo, heldItem.itemID, x, y, z);
			PacketUtil.sendPacketWithInt(PacketUtil.meta, heldItem.getItemDamage(), x, y, z);
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
		TileEntityMultiTankCore getLocations = (TileEntityMultiTankCore) world.getBlockTileEntity(x, y, z);	
		ForgeDirection dir = ForgeDirection.UNKNOWN;

		if (side == 2) dir = ForgeDirection.SOUTH;
		else if (side == 5) dir = ForgeDirection.WEST;
		else if (side == 3) dir = ForgeDirection.NORTH;
		else if (side == 4) dir = ForgeDirection.EAST;
		else dir = ForgeDirection.UNKNOWN;

		if (heldItem != null && (heldItem.itemID == this.blockID || heldItem.itemID == BlockManager.tankSub.blockID))
			return false;

		if (heldItem != null && heldItem.itemID == Item.dyePowder.itemID) {
			getLocations.dyeIndex = heldItem.getItemDamage();
            PacketUtil.sendPacketWithInt(PacketUtil.dyed, heldItem.getItemDamage(), x, y, z);
			world.markBlockForRenderUpdate(x, y, z);
		}	
		else if (heldItem != null && heldItem.itemID == Item.potion.itemID) {
			FluidStack liquid = new FluidStack(DynamicTanks.fluidPotion, FluidContainerRegistry.BUCKET_VOLUME);
			TileEntityMultiTankCore logic = (TileEntityMultiTankCore) world.getBlockTileEntity(x, y, z);

			int amount = logic.fill(dir, liquid, false);
			if (amount == liquid.amount && (logic.storedPotionId == -1 || logic.storedPotionId == heldItem.getItemDamage())) {
				logic.fill(dir, liquid, true);
				ItemUtils.removeSingleItem(heldItem);
				player.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle, 1));
				logic.storedPotionId = heldItem.getItemDamage();
				return true;
			}
		} 
		else if (heldItem != null && heldItem.itemID == Item.glassBottle.itemID && getLocations.storedPotionId != -1 && getLocations.tank.getFluid() != null) {
			FluidStack fillLiquid = getLocations.tank.getFluid();
			if (TEUtil.getBottomTankLiquid(getLocations).amount < 1000)
				return false;
			getLocations.drain(ForgeDirection.UNKNOWN, FluidContainerRegistry.BUCKET_VOLUME, true);
			ItemUtils.removeSingleItem(heldItem);
			player.inventory.addItemStackToInventory(new ItemStack(Item.potion, 1, getLocations.storedPotionId));			
		}
		else if (heldItem == null || (heldItem != null && !FluidContainerRegistry.isBucket(heldItem))) {
			player.openGui(DynamicTanks.instance, ModConfig.GUIIDs.tankCE, world, x, y, z);
			return true;
		}
		else if (heldItem != null)
		{
			FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(player.getCurrentEquippedItem());
			TileEntityMultiTankCore logic = (TileEntityMultiTankCore) world.getBlockTileEntity(x, y, z);
			if (liquid != null)
			{
				int amount = logic.fill(dir, liquid, false);
				if (amount == liquid.amount)
				{
					logic.fill(dir, liquid, true);
					if (!player.capabilities.isCreativeMode) {
						player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemUtils.consumeItem(heldItem));
					}
					return true;
				}
				else
					return false;
			}
			else if (FluidContainerRegistry.isBucket(heldItem))
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
					return false;
				}
			}
		}

		return false;
	}

	public void resizeTank(TileEntityMultiTankCore toResize) {
		if (toResize != null) {
			int calculateSize = (int) (((10 * toResize.connectingTanks) * (toResize.connectingTanks * .175)) * toResize.scalarMultiplier);//+ (int)toResize.totalTankHardness;
			if (toResize != null && toResize.containsLiquid()) {
				FluidStack liquid = toResize.tank.getFluid();
				int amount = toResize.tank.getFluid().amount;// > calculateSize * LiquidContainerRegistry.BUCKET_VOLUME ? calculateSize : toResize.tank.getLiquid().amount; //Check here
				toResize.tank = new FluidTank(liquid.getFluid(), amount, calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME);
			} else if (toResize != null) {
				toResize.tank = new FluidTank(calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME);
			}
		}
	}


	@Override
	public boolean shouldSideBeRendered (IBlockAccess world, int x, int y, int z, int side)
	{
        return shouldSideRender(world, x, y, z, side) ? false : super.shouldSideBeRendered(world, x, y, z, side);
	}


    public boolean shouldSideRender(IBlockAccess world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();

        if(world.getBlockId(x, y, z) == this.blockID || world.getBlockId(x, y, z) == BlockManager.tankSub.blockID) {
            TileEntity tmpTile = world.getBlockTileEntity(x, y, z);
            if(tmpTile instanceof TileEntityMultiTankCore) {
                TileEntityMultiTankCore tile = (TileEntityMultiTankCore) tmpTile;

                if(world.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == this.blockID ||
                        world.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == BlockManager.tankSub.blockID) {

                    TileEntity fTmpTile = world.getBlockTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

                    if(fTmpTile instanceof TileEntityMultiTankCore) {
                        TileEntityMultiTankCore fTile = (TileEntityMultiTankCore) fTmpTile;

                        if(tile.side1 == -1 && fTile.side1 != -1)
                            return false;
                    }
                    else if(fTmpTile instanceof TileEntityMultiTankSub) {
                        TileEntityMultiTankSub fTile = (TileEntityMultiTankSub) fTmpTile;

                        if(tile.side1 == -1 && fTile.side1 != -1)
                            return false;
                    }
                }
            }
            else if(tmpTile instanceof TileEntityMultiTankSub) {
                TileEntityMultiTankSub tile = (TileEntityMultiTankSub) tmpTile;

                if(world.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == this.blockID ||
                        world.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == BlockManager.tankSub.blockID) {

                    TileEntity fTmpTile = world.getBlockTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

                    if(fTmpTile instanceof TileEntityMultiTankCore) {
                        TileEntityMultiTankCore fTile = (TileEntityMultiTankCore) fTmpTile;

                        if(tile.side1 == -1 && fTile.side1 != -1)
                            return false;
                    }
                    else if(fTmpTile instanceof TileEntityMultiTankSub) {
                        TileEntityMultiTankSub fTile = (TileEntityMultiTankSub) fTmpTile;

                        if(tile.side1 == -1 && fTile.side1 != -1)
                            return false;
                    }
                }
            }
            return true;
        }

        return false;
    }

	@Override
	public int getLightValue (IBlockAccess world, int x, int y, int z)
	{
		TileEntity logic = world.getBlockTileEntity(x, y, z);
		if (logic != null && logic instanceof TileEntityMultiTankCore)
			return ((TileEntityMultiTankCore) logic).getBrightness();
		return 0;
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int x, int y, int z, Entity par5Entity) {
		TileEntityMultiTankCore mainCore = (TileEntityMultiTankCore) par1World.getBlockTileEntity(x, y, z);

		if (par5Entity instanceof EntityPlayer && par1World.isBlockIndirectlyGettingPowered(x, y, z) && ((TileEntityMultiTankCore) par1World.getBlockTileEntity(x, y, z)).tank.getFluid() != null) {
			if (((TileEntityMultiTankCore) par1World.getBlockTileEntity(x, y, z)).tank.getFluid().getFluid().getName().equalsIgnoreCase("lava")) {
				((EntityPlayer) par5Entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 3));
				((EntityPlayer) par5Entity).addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 200, 3));
				((EntityPlayer) par5Entity).addPotionEffect(new PotionEffect(Potion.fireResistance.id, 25, 1));
				((EntityPlayer) par5Entity).addPotionEffect(new PotionEffect(Potion.regeneration.id, 10, 1));
			} else if (((TileEntityMultiTankCore) par1World.getBlockTileEntity(x, y, z)).tank.getFluid().getFluid().getName().equalsIgnoreCase("water")) {
				((EntityPlayer) par5Entity).addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 10, 1));
				((EntityPlayer) par5Entity).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 10, 1));
				((EntityPlayer) par5Entity).addPotionEffect(new PotionEffect(Potion.nightVision.id, 10, 1));
				((EntityPlayer) par5Entity).addPotionEffect(new PotionEffect(Potion.hunger.id, 60, 0));
			} else if (((TileEntityMultiTankCore) par1World.getBlockTileEntity(x, y, z)).tank.getFluid() != null && ((TileEntityMultiTankCore) par1World.getBlockTileEntity(x, y, z)).tank.getFluid().fluidID < 4096){
				Block block = Block.blocksList[((TileEntityMultiTankCore) par1World.getBlockTileEntity(x, y, z)).tank.getFluid().fluidID];
				block.onEntityCollidedWithBlock(par1World, x, y, z, par5Entity);
			}
		}
		/* Potion Collision */
		if (par5Entity != null && par5Entity instanceof EntityPlayer && mainCore.storedPotionId != -1 && par1World.isBlockIndirectlyGettingPowered(x, y, z) && ((EntityPlayer) par5Entity).getActivePotionEffects().isEmpty() && TEUtil.getBottomTankLiquid(mainCore) != null && (TEUtil.getBottomTankLiquid(mainCore).amount - (FluidContainerRegistry.BUCKET_VOLUME / 10)) >= 0) {
			collisionEffects.applyPotionEffects((EntityPlayer) par5Entity, mainCore.storedPotionId, false);
			mainCore.drain(ForgeDirection.UNKNOWN, FluidContainerRegistry.BUCKET_VOLUME / 10, true);
			mainCore.updateMe(par1World);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)	{
		float f = 0.0625F;
		return AxisAlignedBB.getAABBPool().getAABB((double)((float)par2 + f), (double)par3, (double)((float)par4 + f), (double)((float)(par2 + 1) - f), (double)((float)(par3 + 1) - f), (double)((float)(par4 + 1) - f));
	}

	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
	{
		return ((TileEntityMultiTankCore) par1World.getBlockTileEntity(par2, par3, par4)).getLiquidAmountScaledForComparator();
	}
}
