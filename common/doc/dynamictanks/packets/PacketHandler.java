package doc.dynamictanks.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import doc.dynamictanks.Utils.ItemUtils;
import doc.dynamictanks.items.ItemManager;
import doc.dynamictanks.tileentity.TileEntityMultiTankSub;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import doc.dynamictanks.Utils.PacketUtil;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class PacketHandler implements IPacketHandler {
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));	
		
		int id, x, y, z;
		float value;
		
		try {
			id = data.readInt();
			value = data.readFloat();
			x = data.readInt();
			y = data.readInt();
			z = data.readInt();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		TileEntity variableChange = ((EntityPlayer) player).worldObj.getBlockTileEntity(x, y, z);
		/*if (variableChange instanceof TileEntityMultiTankSub) {
			if (id == PacketUtil.camo) {
				((TileEntityMultiTankCore) variableChange).side1 = (int) value;
			} else if (id == PacketUtil.meta) {
				((TileEntityMultiTankCore) variableChange).meta1 = (int) value;
			}
		}*/
		if (variableChange instanceof TileEntityMultiTankCore) {
			if (id == PacketUtil.draining) {
				((TileEntityMultiTankCore) variableChange).isDraining = value == 0 ? false : true;
			} else if (id == PacketUtil.increaseSize) {
				((TileEntityMultiTankCore) variableChange).increaseSize(value);
			} else if (id == PacketUtil.decreaseSize) {
				((TileEntityMultiTankCore) variableChange).decreaseSize(value);
			} else if (id == PacketUtil.changeIndex) {
				((TileEntityMultiTankCore) variableChange).selectPostion = (int) value;
			} else if (id == PacketUtil.camo) {
				((TileEntityMultiTankCore) variableChange).side1 = (int) value;
			} else if (id == PacketUtil.meta) {
				((TileEntityMultiTankCore) variableChange).meta1 = (int) value;
			} else if (id == PacketUtil.scalar) {
				((TileEntityMultiTankCore) variableChange).scalarMultiplier = value;
                ((TileEntityMultiTankCore) variableChange).setInventorySlotContents(1, new ItemStack(ItemManager.chipSet, 1, ItemUtils.returnMeta(value)));
                resizeTank((TileEntityMultiTankCore) variableChange, ((EntityPlayer) player).worldObj);
			} else if (id == PacketUtil.TOP) {
				((TileEntityMultiTankCore) variableChange).autoOutput[0] = !((TileEntityMultiTankCore) variableChange).autoOutput[0];
			} else if (id == PacketUtil.FRONT) {
				((TileEntityMultiTankCore) variableChange).autoOutput[1] = !((TileEntityMultiTankCore) variableChange).autoOutput[1];
			} else if (id == PacketUtil.BOTTOM) {
				((TileEntityMultiTankCore) variableChange).autoOutput[2] = !((TileEntityMultiTankCore) variableChange).autoOutput[2];
			} else if (id == PacketUtil.BACK) {
				((TileEntityMultiTankCore) variableChange).autoOutput[3] = !((TileEntityMultiTankCore) variableChange).autoOutput[3];
			} else if (id == PacketUtil.LEFT) {
				((TileEntityMultiTankCore) variableChange).autoOutput[4] = !((TileEntityMultiTankCore) variableChange).autoOutput[4];
			} else if (id == PacketUtil.RIGHT) {
				((TileEntityMultiTankCore) variableChange).autoOutput[5] = !((TileEntityMultiTankCore) variableChange).autoOutput[5];
			} else if (id == PacketUtil.dyed) {
                ((TileEntityMultiTankCore) variableChange).dyeIndex = (int) value;
            }
		} else if(variableChange instanceof TileEntityMultiTankSub) {
            if (id == PacketUtil.camo) {
                ((TileEntityMultiTankSub) variableChange).side1 = (int) value;
            } else if (id == PacketUtil.meta) {
                ((TileEntityMultiTankSub) variableChange).meta1 = (int) value;
            } else if (id == PacketUtil.scalar) {
                ((TileEntityMultiTankSub) variableChange).getCore().setInventorySlotContents(1, new ItemStack(ItemManager.chipSet, 1, ItemUtils.returnMeta(value)));
                resizeTank(((TileEntityMultiTankSub) variableChange).getCore(), ((EntityPlayer) player).worldObj);
            } else if (id == PacketUtil.dyed) {
                ((TileEntityMultiTankSub) variableChange).dyeIndex = (int) value;
            }
        }
        ((EntityPlayer) player).worldObj.markBlockForUpdate(x, y, z);
	}

    public static void resizeTank(TileEntityMultiTankCore toResize, World world) {
        if (toResize != null) {
            int calculateSize = (int) (((10 * toResize.connectingTanks) * (toResize.connectingTanks * .195)) * toResize.scalarMultiplier);//+ (int)toResize.totalTankHardness;
            if (toResize != null && toResize.containsLiquid()) {
                FluidStack liquid = toResize.tank.getFluid();
                int amount = toResize.tank.getFluid().amount;// > calculateSize * LiquidContainerRegistry.BUCKET_VOLUME ? calculateSize : toResize.tank.getLiquid().amount; //Check here
                toResize.tank.setCapacity(calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME); //= new FluidTank(liquid.getFluid(), amount, calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME);
            } else if (toResize != null) {
                toResize.tank.setCapacity(calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME); //= new FluidTank(calculateSize < 10 ? 10 * FluidContainerRegistry.BUCKET_VOLUME : calculateSize * FluidContainerRegistry.BUCKET_VOLUME);
            }
            world.markBlockForUpdate(toResize.xCoord, toResize.yCoord, toResize.zCoord);
        }
    }
	
	/*public void receiveCustomPacket(Packet250CustomPayload pkt) {
	      int type = pkt.data[0];
	      switch (type) {
	         case 0:
	            handleTileEntityPacket(pkt);
	            break;
	      }
	   }
	   
	   void handleTileEntityPacket(Packet250CustomPayload pkt) {
	      NBTTagCompound tag = PacketUtil.nbtFromPacket(pkt);
	      int x = tag.getInteger("x");
	      int y = tag.getInteger("y");
	      int z = tag.getInteger("z");
	      World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[0];
	      if (world.blockExists(x, y, z)) {
	         TileEntity te = world.getBlockTileEntity(x, y, z);
	         if (te != null) {
	            te.readFromNBT(tag);
	            te.onInventoryChanged();
	         }
	      }
	   }*/
	
}
