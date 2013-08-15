package doc.dynamictanks.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import doc.dynamictanks.DynamicTanks;
import doc.dynamictanks.Utils.PacketUtil;
import doc.dynamictanks.Utils.ParticleUtils;
import doc.dynamictanks.Utils.TEUtil;
import doc.dynamictanks.block.BlockManager;
import doc.dynamictanks.common.ModConfig;

public class TileEntityMultiTankSub extends TileEntity implements IFluidHandler, IPeripheral {
	
	public FluidTank tank;
	TileEntityMultiTankCore tileEntityCore;
	int coreX;
	int coreY;
	int coreZ;
	public int side0 = -1, side1 = -1, side2 = -1, side3 = -1, side4 = -1, side5 = -1;
	public int meta0 = 0, meta1 = 0, meta2 = 0, meta3 = 0, meta4 = 0, meta5 = 0;
	public int dyeIndex = -1;
	
	public void setCore(TileEntityMultiTankCore core)
	{
		coreX = core.xCoord;
		coreY = core.yCoord;
		coreZ = core.zCoord;
		tileEntityCore = core;
	}
	
	public TileEntityMultiTankCore getCore()
	{
		if(tileEntityCore == null)
			if (worldObj.getBlockTileEntity(coreX, coreY, coreZ) instanceof TileEntityMultiTankCore)
				tileEntityCore = (TileEntityMultiTankCore)worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		
		return tileEntityCore;
	}
	
	public float getLiquidAmountScaled()
	{
		if (getCore().tank.getCapacity() < getCore().tank.getFluid().amount) {
			return worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord) != null ? (float) (this.getCore().tank.getCapacity() - this.getCore().renderOffset) / (float) (this.getCore().tank.getCapacity() * 1.00F) : (float) (this.getCore().tank.getCapacity() - this.getCore().renderOffset) / (float) (this.getCore().tank.getCapacity() * 1.01F);
		}
		return worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord) != null ? (float) (this.getCore().tank.getFluid().amount - this.getCore().renderOffset) / (float) (this.getCore().tank.getCapacity() * 1.00F) : (float) (this.getCore().tank.getFluid().amount - this.getCore().renderOffset) / (float) (this.getCore().tank.getCapacity() * 1.01F);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		coreX = tagCompound.getInteger("CoreX");
		coreY = tagCompound.getInteger("CoreY");
		coreZ = tagCompound.getInteger("CoreZ");
		side0 = tagCompound.getInteger("side0");
		side1 = tagCompound.getInteger("side1");
		side2 = tagCompound.getInteger("side2");
		side3 = tagCompound.getInteger("side3");
		side4 = tagCompound.getInteger("side4");
		side5 = tagCompound.getInteger("side5");
		meta0 = tagCompound.getInteger("meta0");
		meta1 = tagCompound.getInteger("meta1");
		meta2 = tagCompound.getInteger("meta2");
		meta3 = tagCompound.getInteger("meta3");
		meta4 = tagCompound.getInteger("meta4");
		meta5 = tagCompound.getInteger("meta5");
		dyeIndex = tagCompound.getInteger("dyeColor");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("CoreX", coreX);
		tagCompound.setInteger("CoreY", coreY);
		tagCompound.setInteger("CoreZ", coreZ);
		tagCompound.setInteger("side0", side0);
		tagCompound.setInteger("side1", side1);
		tagCompound.setInteger("side2", side2);
		tagCompound.setInteger("side3", side3);
		tagCompound.setInteger("side4", side4);
		tagCompound.setInteger("side5", side5);
		tagCompound.setInteger("meta0", meta0);
		tagCompound.setInteger("meta1", meta1);
		tagCompound.setInteger("meta2", meta2);
		tagCompound.setInteger("meta3", meta3);
		tagCompound.setInteger("meta4", meta4);
		tagCompound.setInteger("meta5", meta5);
		tagCompound.setInteger("dyeColor", dyeIndex);
	}
	
	@Override
	public Packet getDescriptionPacket () {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public void onDataPacket (INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.customParam1);
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return this.getCore() != null ? this.getCore().fill(from, resource, doFill) : null;
	}

	/*@Override
	public int fill(int tankIndex, FluidStack resource, boolean doFill) {
		return this.getCore() != null ? this.getCore().fill(tankIndex, resource, doFill) : null;
	}*/

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.getCore() != null ? this.getCore().drain(from, maxDrain, doDrain) : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return this.getCore() != null ? this.getCore().drain(from, resource, doDrain) : null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return this.getCore() != null ? new FluidTankInfo[] { this.getCore().tank.getInfo() } : null;
	}
	
/*	@Override
	public ILiquidTank[] getTanks (ForgeDirection direction)
	{
		return this.getCore() != null ? this.getCore().getTanks(direction) : null;
	}

	@Override
	public ILiquidTank getTank (ForgeDirection direction, LiquidStack type)
	{
		return this.getCore() != null ? this.getCore().getTank(direction, type) : null;
	}*/
	
	@Override
	public void updateEntity() 
	{
		if (worldObj.isRemote && this.getCore() != null && TEUtil.getBottomTankLiquid(this.getCore()) != null && TEUtil.getBottomTankLiquid(this.getCore()).getFluid() == DynamicTanks.fluidPotion && ModConfig.BooleanVars.particles && side1 == -1) ParticleUtils.spawnCustomParticle("coloredSwirl", worldObj, xCoord, yCoord, zCoord, 45);
		
		if (this.getCore() != null && this.worldObj.getBlockId(coreX, coreY, coreZ) != BlockManager.tankCore.blockID) {
			tileEntityCore = null;
			coreX = -1;
			coreY = -1;
			coreZ = -1;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		
		if (this.getCore() != null && !(this.worldObj.getBlockTileEntity(coreX, coreY, coreZ) instanceof TileEntityMultiTankCore)) {
			tileEntityCore = null;
			coreX = -1;
			coreY = -1;
			coreZ = -1;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		
		if (getCore() == null) {
			TEUtil.setCoreBNCore(worldObj, xCoord, yCoord, zCoord);
			TEUtil.setCoreBNSub(worldObj, xCoord, yCoord, zCoord);
			if (getCore() != null && worldObj.getBlockTileEntity(coreX, coreY, coreZ) instanceof TileEntityMultiTankCore) {
				getCore().connectingTanks++;
                PacketUtil.sendPacketWithInt(PacketUtil.scalar, getCore().scalarMultiplier, getCore().xCoord, getCore().yCoord, getCore().zCoord);
				worldObj.markBlockForUpdate(coreX, coreY, coreZ);
			}
		}
	}
	
	/*
     * Helpers
     */
    
	public void resizeTank(TileEntityMultiTankCore toResize) {
		if (toResize != null) {
			int calculateSize = (int) (((10 * toResize.connectingTanks) * (toResize.connectingTanks * .195)) * toResize.scalarMultiplier);//+ (int)toResize.totalTankHardness;
			toResize.tank.setCapacity(calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME); //= new FluidTank(calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME);
		}
	}
	
    public TileEntityMultiTankCore getBottomTank() {

    	TileEntityMultiTankCore lastTank = this.getCore();

		while (true) {
			TileEntityMultiTankCore below = getTankBelow(lastTank);
			if (below != null) {
				lastTank = below;
			} else {
				break;
			}
		}

		return lastTank;
	}
    
	public TileEntityMultiTankCore getTankBelow(TileEntityMultiTankCore tile) {
		TileEntity below = tile.worldObj.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
		if (below instanceof TileEntityMultiTankCore) {
			return (TileEntityMultiTankCore) below;
		} else if (below instanceof TileEntityMultiTankSub) {
			return ((TileEntityMultiTankSub) below).getCore();
		} 
		return null;
	}

	public TileEntityMultiTankCore getTankAbove(TileEntityMultiTankCore tile) {
		TileEntity above = tile.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
		if (above instanceof TileEntityMultiTankCore) {
			return (TileEntityMultiTankCore) above;
		} else if (above instanceof TileEntityMultiTankSub) {
			return ((TileEntityMultiTankSub) above).getCore();
		} 
		return null;
	}

	public void moveLiquidBelow() {
		TileEntityMultiTankCore below = getTankBelow(this.getCore());
		if (below == null) {
			return;
		}

		int used = below.tank.fill(tank.getFluid(), true);
		if (used > 0) {
			tank.drain(used, true);
		}
	}
	
	@Override
	public String getType() {
		return "tank_Node";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "getFluid", "getAmount", "getCapacity", "getNeighbors", "getEnergy", "setCamoBlock", "setCamoBlockWithMeta" };
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context,
			int method, Object[] arguments) throws Exception {
		switch (method) {
		case 0:
			return new Object[] { getCore().tank.getFluid().getFluid().getName() };
		case 1: 
			return new Object[] { getCore().tank.getFluidAmount() };
		case 2: 
			return new Object[] { getCore().tank.getCapacity() };
		case 3:
			return new Object[] { getCore().connectingTanks };
		case 4:
			return new Object[] { getCore().powerProvider.getEnergyStored() };
		case 5: 
			if (arguments[0] instanceof Double && ((Double) arguments[0]).intValue() < 4096 && Block.blocksList[((Double) arguments[0]).intValue()] != null) {
				this.side1 = ((Double) arguments[0]).intValue();
				PacketUtil.sendPacketWithInt(PacketUtil.camo, ((Double) arguments[0]).intValue(), xCoord, yCoord, zCoord);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return new Object[] { true };
			} else 
				return new Object[] { false };
		case 6:
			if ((arguments[0] instanceof Double && arguments[1] instanceof Double) && ((Double) arguments[0]).intValue() < 4096 && Block.blocksList[((Double) arguments[0]).intValue()] != null) {
				this.side1 = ((Double) arguments[0]).intValue();
				this.meta1 = ((Double) arguments[1]).intValue();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return new Object[] { true };
			} else 
				return new Object[] { false };
		} 
		return new Object[] { "Wrong" };
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
				
	}

	@Override
	public void detach(IComputerAccess computer) {
		// TODO Auto-generated method stub
		
	}

}
