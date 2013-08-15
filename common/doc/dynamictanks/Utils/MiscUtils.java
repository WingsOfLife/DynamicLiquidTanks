package doc.dynamictanks.Utils;

import java.awt.Color;

import doc.dynamictanks.block.BlockManager;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class MiscUtils {

	public static class miscVariables {
		
		public static boolean camoKeyDown = false;
		
	}
	
	public static float randomNumber(float Max, float Min) {
		return Min + (int)(Math.random() * ((Max - Min) + 1));
	}
	
	public static Color hex2Rgb(String colorStr) {
	    return new Color(
	            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
	            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
	            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}
	
	public static boolean atLeastOneTrue(boolean[] list) {
		for (int i = 0; i < list.length; i++) {
			if (list[i]) return true;
		}
		return false;
	}
	
	public static int countTrue(boolean[] list) {
		int numTrue = 0;
		for (int i = 0; i < list.length; i++) {
			if (list[i]) numTrue++;
		}
		return numTrue;
	}
	
	public static void getBlockTop(FluidStack fluid, TileEntityMultiTankCore drain, World world, int x, int y, int z) {
		if (world.getBlockTileEntity(x, y + 1, z) instanceof IFluidHandler && world.getBlockId(x, y + 1, z) != BlockManager.tankCore.blockID && world.getBlockId(x, y + 1, z) != BlockManager.tankSub.blockID) {
			IFluidHandler casted = ((IFluidHandler) world.getBlockTileEntity(x, y + 1, z));
			if (casted.canFill(ForgeDirection.UNKNOWN, fluid.getFluid())) {
				casted.fill(ForgeDirection.UNKNOWN, fluid, true);
				drain.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
			}
		}
	}
	
	public static void getBlockBottom(FluidStack fluid, TileEntityMultiTankCore drain, World world, int x, int y, int z) {
		if (world.getBlockTileEntity(x, y - 1, z) instanceof IFluidHandler && world.getBlockId(x, y - 1, z) != BlockManager.tankCore.blockID && world.getBlockId(x, y - 1, z) != BlockManager.tankSub.blockID) {
			IFluidHandler casted = ((IFluidHandler) world.getBlockTileEntity(x, y - 1, z));
			if (casted.canFill(ForgeDirection.UNKNOWN, fluid.getFluid())) {
				casted.fill(ForgeDirection.UNKNOWN, fluid, true);
				drain.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
			}
		}
	}
	
	public static void getBlockNorth(FluidStack fluid, TileEntityMultiTankCore drain, World world, int x, int y, int z) {
		if (world.getBlockTileEntity(x, y, z - 1) instanceof IFluidHandler && world.getBlockId(x, y, z - 1) != BlockManager.tankCore.blockID && world.getBlockId(x, y, z - 1) != BlockManager.tankSub.blockID) {
			IFluidHandler casted = ((IFluidHandler) world.getBlockTileEntity(x, y, z - 1));
			if (casted.canFill(ForgeDirection.UNKNOWN, fluid.getFluid())) {
				casted.fill(ForgeDirection.UNKNOWN, fluid, true);
				drain.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
			}
		}
	}
	
	public static void getBlockSouth(FluidStack fluid, TileEntityMultiTankCore drain, World world, int x, int y, int z) {
		if (world.getBlockTileEntity(x, y, z + 1) instanceof IFluidHandler && world.getBlockId(x, y, z + 1) != BlockManager.tankCore.blockID && world.getBlockId(x, y, z + 1) != BlockManager.tankSub.blockID) {
			IFluidHandler casted = ((IFluidHandler) world.getBlockTileEntity(x, y, z + 1));
			if (casted.canFill(ForgeDirection.UNKNOWN, fluid.getFluid())) {
				casted.fill(ForgeDirection.UNKNOWN, fluid, true);
				drain.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
			}
		}
	}
	
	public static void getBlockEast(FluidStack fluid, TileEntityMultiTankCore drain, World world, int x, int y, int z) {
		if (world.getBlockTileEntity(x + 1, y, z) instanceof IFluidHandler && world.getBlockId(x + 1, y, z) != BlockManager.tankCore.blockID && world.getBlockId(x + 1, y, z) != BlockManager.tankSub.blockID) {
			IFluidHandler casted = ((IFluidHandler) world.getBlockTileEntity(x + 1, y, z));
			if (casted.canFill(ForgeDirection.UNKNOWN, fluid.getFluid())) {
				casted.fill(ForgeDirection.UNKNOWN, fluid, true);
				drain.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
			}
		}
	}
	
	public static void getBlockWest(FluidStack fluid, TileEntityMultiTankCore drain, World world, int x, int y, int z) {
		if (world.getBlockTileEntity(x - 1, y, z) instanceof IFluidHandler && world.getBlockId(x - 1, y, z) != BlockManager.tankCore.blockID && world.getBlockId(x - 1, y, z) != BlockManager.tankSub.blockID) {
			IFluidHandler casted = ((IFluidHandler) world.getBlockTileEntity(x - 1, y, z));
			if (casted.canFill(ForgeDirection.UNKNOWN, fluid.getFluid())) {
				casted.fill(ForgeDirection.UNKNOWN, fluid, true);
				drain.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
			}
		}
	}
	
}
