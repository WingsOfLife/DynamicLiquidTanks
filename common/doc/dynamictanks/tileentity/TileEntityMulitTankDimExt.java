package doc.dynamictanks.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import doc.dynamictanks.Utils.ItemUtils;
import doc.dynamictanks.Utils.ParticleUtils;
import doc.dynamictanks.Utils.TEUtil;
import doc.dynamictanks.block.BlockManager;
import doc.dynamictanks.block.blockEventIds;
import doc.dynamictanks.common.ModConfig;
import doc.dynamictanks.items.ItemManager;

public class TileEntityMulitTankDimExt extends TileEntity implements IFluidHandler, IPowerReceptor, IPeripheral, IInventory, ISidedInventory {

	private ItemStack[] inv;
	
	//private Ticket chunkTicket;

	public FluidTank tank;

	public PowerHandler powerProvider;

	TileEntityMultiTankCore tileEntityCore;

	public int coreX = 0;
	public int coreY = 0;
	public int coreZ = 0;
	public int dyeIndex = -1;
	
	public int endDay = -1;
	
	public long endTime = -1;
	
	public boolean isActive = false;
	public boolean enderPowered = false;
	
	public int coreAmount = 0;
	public int coreCapacity = 0;
	public int fluidId = -1;

	public TileEntityMulitTankDimExt() {

		inv = new ItemStack[2];

		powerProvider = new PowerHandler(this, Type.MACHINE);
		powerProvider.configure(1, 200, 25, 1000);
	}

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
	
	public void nullCore() {
		tileEntityCore = null;
		coreX = -1;
		coreY = -1;
		coreZ = -1;
	}

	public void trackTime() {
		
		int currentDay = (int) ((worldObj.getWorldTime() / 1000) / 24);
		long currentTime = worldObj.getTotalWorldTime();
		
		if (getStackInSlot(1) != null && getStackInSlot(1).itemID == Item.enderPearl.itemID && !isActive) {
			ItemStack toReturn = ItemUtils.consumeItem(getStackInSlot(1));
			
			endDay = currentDay + 1;
			endTime = currentTime;
			
			isActive = true;
			enderPowered = true;
			
			setInventorySlotContents(1, toReturn);
			//System.out.println("Started running on " + currentDay + " :: " + currentTime + " :: " + isActive);
		}
		
		if (currentDay > endDay && currentTime > endTime && isActive == true) {
			if (getStackInSlot(1) != null && getStackInSlot(1).itemID == Item.enderPearl.itemID) {
				ItemStack toReturn = ItemUtils.consumeItem(getStackInSlot(1));
				
				endDay = currentDay + 1;
				endTime = currentTime;
				
				isActive = true;
				
				setInventorySlotContents(1, toReturn);
			} else {
				isActive = false;
				enderPowered = false;
				//System.out.println("Stopped running on " + currentDay + " :: " + currentTime);
			}
		}
		
	}
	
	@Override
	public void updateEntity() {	
		
		trackTime();
		
		if (worldObj.isRemote && isActive && ModConfig.BooleanVars.particles) ParticleUtils.spawnParticle("portal", worldObj, xCoord, yCoord, zCoord);
		
		if (getStackInSlot(0) != null && getStackInSlot(0).itemID == ItemManager.linkCard.itemID && getStackInSlot(0).stackTagCompound != null && getCore() == null) {
			ItemStack applyInfo = getStackInSlot(0);
			TileEntity newCore = worldObj.getBlockTileEntity(applyInfo.stackTagCompound.getInteger("X"), applyInfo.stackTagCompound.getInteger("Y"), applyInfo.stackTagCompound.getInteger("Z"));
			if (newCore instanceof TileEntityMultiTankCore) {
				setCore((TileEntityMultiTankCore) newCore);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
		
		if (getStackInSlot(0) == null && getCore() != null) {
			nullCore();
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		
		if (!worldObj.isRemote) {
			TileEntityMultiTankCore myCore = (TileEntityMultiTankCore) worldObj.getBlockTileEntity(coreX, coreY, coreZ);
			
			if (getCore() == null)
				return;
			
			 if (myCore == null || myCore.tank.getFluid() == null)
				 return;
			
			coreAmount = TEUtil.getBottomTankForAmount(getCore()) + getCore().tank.getFluid().amount;
			coreCapacity = TEUtil.getBottomTankForCapacity(getCore()) + getCore().tank.getCapacity();
			fluidId = fluidId != -1 ? TEUtil.getBottomTankLiquid(getCore()).fluidID : FluidRegistry.getFluidID("water");
			
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockManager.tankDimension.blockID, blockEventIds.fluidAmountId, coreAmount / FluidContainerRegistry.BUCKET_VOLUME);
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockManager.tankDimension.blockID, blockEventIds.totalCoreId, coreCapacity / FluidContainerRegistry.BUCKET_VOLUME);
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockManager.tankDimension.blockID, blockEventIds.storeFluidId, fluidId);
		}
		
		if (worldObj.isRemote)
			return;
		
		if (powerProvider.getEnergyStored() <= 25 && !enderPowered) {
			doWork(powerProvider);
		}
	}

	@Override
	public boolean receiveClientEvent(int id, int value) {
		if (worldObj.isRemote) {
			switch (id) {
				case blockEventIds.fluidAmountId: coreAmount = value; break;
				case blockEventIds.totalCoreId: coreCapacity = value; break;
				case blockEventIds.storeFluidId: fluidId = value; break;
			}			
		}		
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		coreX = tagCompound.getInteger("CoreX");
		coreY = tagCompound.getInteger("CoreY");
		coreZ = tagCompound.getInteger("CoreZ");

		endDay = tagCompound.getInteger("EndDay");
		endTime = tagCompound.getLong("EndTime");
		
		dyeIndex = tagCompound.getInteger("dyeIndex");
		
		isActive = tagCompound.getBoolean("active");
		enderPowered = tagCompound.getBoolean("endered");
		
		NBTTagList tagList = tagCompound.getTagList("Inventory");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("CoreX", coreX);
		tagCompound.setInteger("CoreY", coreY);
		tagCompound.setInteger("CoreZ", coreZ);
		
		tagCompound.setInteger("EndDay", endDay);
		tagCompound.setLong("EndTime", endTime);
		
		tagCompound.setInteger("dyeIndex", dyeIndex);
		
		tagCompound.setBoolean("active", isActive);
		tagCompound.setBoolean("endered", enderPowered);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			ItemStack stack = inv[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
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
		//worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return this.getCore() != null && isActive ? this.getCore().fill(from, resource, doFill) : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.getCore() != null && isActive ? this.getCore().drain(from, maxDrain, doDrain) : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return this.getCore() != null && isActive ? this.getCore().drain(from, resource, doDrain) : null;
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

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		return powerProvider.getPowerReceiver();
	}

	@Override
	public void doWork(PowerHandler workProvider) {

		if (this.worldObj.isRemote)
			return;

		if (powerProvider.useEnergy(25, 25, true) < 25) {
			isActive = false;
			return;
		}

		if (powerProvider.getEnergyStored() >= 25) {
			doWork(workProvider);
			isActive = true;
		}		

	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}

	@Override
	public String getType() {
		return "dimensional_Tank_Node";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "getFluid", "getAmount", "getCapacity", "getEnergy", "getCore", "setCoreLocation" };
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
			return new Object[] { powerProvider.getEnergyStored() };
		case 4:
			return new Object[] { this.coreX, this.coreY, this.coreZ };
		case 5:
			if (arguments[0] instanceof Double && arguments[1] instanceof Double && arguments[2] instanceof Double) {
				if (this.worldObj.getBlockTileEntity(((Double) arguments[0]).intValue(), ((Double) arguments[1]).intValue(), ((Double) arguments[2]).intValue()) instanceof TileEntityMultiTankCore) {
					this.setCore((TileEntityMultiTankCore) worldObj.getBlockTileEntity(((Double) arguments[0]).intValue(), ((Double) arguments[1]).intValue(), ((Double) arguments[2]).intValue()));
					/*this.coreX = ((Double) arguments[0]).intValue();
					this.coreY = ((Double) arguments[1]).intValue();
					this.coreZ = ((Double) arguments[2]).intValue();*/
					return new Object[] { true };
				}
			}
			return new Object[] { false };
		} 
		return new Object[] { "Failed to find method." };
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

	}

	// GUI/Inventory Stuff

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		} 

	}

	@Override
	public String getInvName() {
		return "dynamictanks.DimExt";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
				player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] { 1 };
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		if (itemstack.itemID == Item.enderPearl.itemID) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}
	
	//Chunk Stuff
	/*@Override
	public void invalidate() {		
		ForgeChunkManager.releaseTicket(chunkTicket);
		super.invalidate();		
	}
	
	private void setBoundaries() {
		if (chunkTicket == null)
			chunkTicket = ForgeChunkManager.requestTicket(DynamicTanks.instance, worldObj, ForgeChunkManager.Type.NORMAL);
		
		if (chunkTicket == null)
			return;
		
		chunkTicket.getModData().setInteger("CoreX", xCoord);
		chunkTicket.getModData().setInteger("CoreY", yCoord);
		chunkTicket.getModData().setInteger("CoreZ", zCoord);
		
		ForgeChunkManager.forceChunk(chunkTicket, new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4));
		
		forceChunkLoading(chunkTicket);
	}
	
	public void forceChunkLoading(Ticket ticket) {
		if (chunkTicket == null)
			chunkTicket = ticket;
		
		Set<ChunkCoordIntPair> chunks = Sets.newHashSet();
		ChunkCoordIntPair quarryChunk = new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4);
		chunks.add(quarryChunk);
		ForgeChunkManager.forceChunk(ticket, quarryChunk);

		for (int chunkX = 0 >> 4; chunkX <= 5 >> 4; chunkX++) {
			for (int chunkZ = 0 >> 4; chunkZ <= 5 >> 4; chunkZ++) {
				ChunkCoordIntPair chunk = new ChunkCoordIntPair(chunkX, chunkZ);
				ForgeChunkManager.forceChunk(ticket, chunk);
				chunks.add(chunk);
			}
		}
	}*/

}
