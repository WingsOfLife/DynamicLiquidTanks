package doc.dynamictanks.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
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
			} else if (id == PacketUtil.TOP) {
				((TileEntityMultiTankCore) variableChange).autoOutput[0] = setValue(((TileEntityMultiTankCore) variableChange).autoOutput[0]);
			} else if (id == PacketUtil.FRONT) {
				((TileEntityMultiTankCore) variableChange).autoOutput[1] = setValue(((TileEntityMultiTankCore) variableChange).autoOutput[1]);
			} else if (id == PacketUtil.BOTTOM) {
				((TileEntityMultiTankCore) variableChange).autoOutput[2] = setValue(((TileEntityMultiTankCore) variableChange).autoOutput[2]);
			} else if (id == PacketUtil.BACK) {
				((TileEntityMultiTankCore) variableChange).autoOutput[3] = setValue(((TileEntityMultiTankCore) variableChange).autoOutput[3]);
			} else if (id == PacketUtil.LEFT) {
				((TileEntityMultiTankCore) variableChange).autoOutput[4] = setValue(((TileEntityMultiTankCore) variableChange).autoOutput[4]);
			} else if (id == PacketUtil.RIGHT) {
				((TileEntityMultiTankCore) variableChange).autoOutput[5] = setValue(((TileEntityMultiTankCore) variableChange).autoOutput[5]);
			}
		} 
	}
	
	public boolean setValue(boolean value) {
		if (value == true) return false;
		else return true;
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
