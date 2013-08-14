package doc.dynamictanks.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import doc.dynamictanks.block.BlockManager;
import doc.dynamictanks.client.particle.FXLiquidSpray;
import doc.dynamictanks.client.particle.ParticleEffects;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;
import doc.dynamictanks.tileentity.TileEntityMultiTankSub;

public class TEUtil {

	public static void setCoreBNCore(World world, int x, int y, int z) {
		if (world.getBlockId(x - 1, y, z) == BlockManager.tankCore.blockID) {
			((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).setCore((TileEntityMultiTankCore) world.getBlockTileEntity(x - 1, y, z));
		} else if (world.getBlockId(x + 1, y, z) == BlockManager.tankCore.blockID) {
			((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).setCore((TileEntityMultiTankCore) world.getBlockTileEntity(x + 1, y, z));
		} else if (world.getBlockId(x, y, z - 1) == BlockManager.tankCore.blockID) {
			((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).setCore((TileEntityMultiTankCore) world.getBlockTileEntity(x, y, z - 1));
		} else if (world.getBlockId(x, y, z + 1) == BlockManager.tankCore.blockID) {
			((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).setCore((TileEntityMultiTankCore) world.getBlockTileEntity(x, y, z + 1));
		}
	}

	public static void setCoreBNSub(World world, int x, int y, int z) {
		if (world.getBlockId(x - 1, y, z) == BlockManager.tankSub.blockID && ((TileEntityMultiTankSub) world.getBlockTileEntity(x - 1, y, z)).getCore() != null) {
			((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).setCore(((TileEntityMultiTankSub) world.getBlockTileEntity(x - 1, y, z)).getCore());
		} else if (world.getBlockId(x + 1, y, z) == BlockManager.tankSub.blockID && ((TileEntityMultiTankSub)world.getBlockTileEntity(x + 1, y, z)).getCore() != null) {
			((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).setCore(((TileEntityMultiTankSub) world.getBlockTileEntity(x + 1, y, z)).getCore());
		} else if (world.getBlockId(x, y, z - 1) == BlockManager.tankSub.blockID && ((TileEntityMultiTankSub)world.getBlockTileEntity(x, y, z - 1)).getCore() != null) {
			((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).setCore(((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z - 1)).getCore());
		} else if (world.getBlockId(x, y, z + 1) == BlockManager.tankSub.blockID && ((TileEntityMultiTankSub)world.getBlockTileEntity(x, y, z + 1)).getCore() != null) {
			((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z)).setCore(((TileEntityMultiTankSub) world.getBlockTileEntity(x, y, z + 1)).getCore());
		}
	}
	
	public static TileEntityMultiTankCore getTopTank(TileEntityMultiTankCore me) {

		TileEntityMultiTankCore lastTank = me;

		while (true) {
			TileEntityMultiTankCore above = getTankAbove(lastTank);
			if (above != null) {
				lastTank = above;
			} else {
				break;
			}
		}

		return lastTank;
	}
	
	public static int getTotalCapacity(TileEntityMultiTankCore me) {
		return TEUtil.getTopTankForCapacity(me) + TEUtil.getBottomTankForCapacity(me) + me.tank.getCapacity();
	}

	public static int getTopTankForCapacity(TileEntityMultiTankCore me) {

		TileEntityMultiTankCore lastTank = me;
		int totalCapacity = 0;

		while (true) {
			TileEntityMultiTankCore above = getTankAbove(lastTank);
			if (above != null) {
				lastTank = above;
				totalCapacity += above.tank.getCapacity();
			} else {
				break;
			}
		}

		return totalCapacity;
	}

	public static int getBottomTankForCapacity(TileEntityMultiTankCore me) {

		TileEntityMultiTankCore lastTank = me;
		int totalCapacity = 0;

		while (true) {
			TileEntityMultiTankCore below = getTankBelow(lastTank);
			if (below != null) {
				lastTank = below;
				totalCapacity += below.tank.getCapacity();
			} else {
				break;
			}
		}

		return totalCapacity;
	}

	//For count
	public static int getTankCount(TileEntityMultiTankCore me) {
		return TEUtil.getTopTankCount(me) + TEUtil.getBottomTankCount(me) + 1;
	}

	public static int getTopTankCount(TileEntityMultiTankCore me) {

		TileEntityMultiTankCore lastTank = me;
		int totalAmount = 0;

		while (true) {
			TileEntityMultiTankCore above = getTankAbove(lastTank);
			if (above != null) {
				lastTank = above;
				totalAmount ++;
			} else {
				break;
			}
		}

		return totalAmount;
	}

	public static int getBottomTankCount(TileEntityMultiTankCore me) {

		TileEntityMultiTankCore lastTank = me;
		int totalCapacity = 0;

		while (true) {
			TileEntityMultiTankCore below = getTankBelow(lastTank);
			if (below != null) {
				lastTank = below;
				totalCapacity ++;
			} else {
				break;
			}
		}

		return totalCapacity;
	}
	
	//For amount
	public static int getTotalAmount(TileEntityMultiTankCore me) {
		int addition = me.tank.getFluid() != null ? me.tank.getFluid().amount : 0;
		return TEUtil.getTopTankForAmount(me) + TEUtil.getBottomTankForAmount(me) + addition;

	}

	public static int getTopTankForAmount(TileEntityMultiTankCore me) {

		TileEntityMultiTankCore lastTank = me;
		int totalAmount = 0;

		while (true) {
			TileEntityMultiTankCore above = getTankAbove(lastTank);
			if (above != null) {
				lastTank = above;
				totalAmount += above.tank.getFluid() != null ? above.tank.getFluid().amount : 0;
			} else {
				break;
			}
		}

		return totalAmount;
	}

	public static int getBottomTankForAmount(TileEntityMultiTankCore me) {

		TileEntityMultiTankCore lastTank = me;
		int totalCapacity = 0;

		while (true) {
			TileEntityMultiTankCore below = getTankBelow(lastTank);
			if (below != null) {
				lastTank = below;
				totalCapacity += below.tank.getFluid() != null ? below.tank.getFluid().amount : 0;
			} else {
				break;
			}
		}

		return totalCapacity;
	}

	//end amount

	private static TileEntityMultiTankCore getTankBelow(TileEntityMultiTankCore tile) { 
		TileEntity below = tile.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord - 1, tile.zCoord);
		if (below instanceof TileEntityMultiTankCore) {
			return (TileEntityMultiTankCore) below;
		} else if (below instanceof TileEntityMultiTankSub) {
			return ((TileEntityMultiTankSub) tile.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord - 1, tile.zCoord)).getCore();
		} 
		return null;
	}

	private static TileEntityMultiTankCore getTankAbove(TileEntityMultiTankCore tile) {
		TileEntity above = tile.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord + 1, tile.zCoord);
		if (above instanceof TileEntityMultiTankCore) {
			return (TileEntityMultiTankCore) above;
		} else if (above instanceof TileEntityMultiTankSub) {
			return ((TileEntityMultiTankSub) tile.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord + 1, tile.zCoord)).getCore();
		} 
		return null;
	}

	public static int getLiquidAmountScaledForGUI(int amount, int capacity)
	{
		double f = (((amount * 0.01) / (capacity * 0.01)) * 65.0D);
		if (f > 65) {
			return 65;
		}
		return (int) f;
	}

	public static FluidStack getBottomTankLiquid(TileEntityMultiTankCore me) {

		TileEntityMultiTankCore lastTank = me;

		while (true) {
			TileEntityMultiTankCore below = getTankBelow(lastTank);
			if (below != null) {
				lastTank = below;
			} else {
				break;
			}
		}

		return lastTank.tank.getFluid() != null ? lastTank.tank.getFluid() : null;
	}

	public static void resizeTank(TileEntityMultiTankCore toResize) {
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
	
	public static boolean getTankInDirection(World world, int x, int y, int z, ForgeDirection direction) {
		int xOffset = direction.offsetX;
		int yOffset = direction.offsetY;
		int zOffset = direction.offsetZ;
		if (world.getBlockTileEntity(x, y, z) instanceof TileEntityMultiTankCore || world.getBlockTileEntity(x, y, z) instanceof TileEntityMultiTankSub) {
			return true;
		}
		return false;
		
	}

}