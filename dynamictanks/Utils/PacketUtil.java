package doc.dynamictanks.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.PacketDispatcher;

public class PacketUtil {

	public static final int draining = 0;
	public static final int increaseSize = 1;
	public static final int decreaseSize = 2;
	public static final int changeIndex = 3;
	public static final int camo = 4;
	public static final int meta = 5;
	public static final int scalar = 6;
	public static final int TOP = 7;
	public static final int FRONT = 8;
	public static final int BOTTOM = 9;
	public static final int BACK = 10;
	public static final int LEFT = 11;
	public static final int RIGHT = 12;

	public static void sendPacketWithInt(int id, float value, int x, int y, int z) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(16);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(id);
			outputStream.writeFloat(value);
			outputStream.writeInt(x);
			outputStream.writeInt(y);
			outputStream.writeInt(z);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "dynamicTanks";
		packet.data = bos.toByteArray();
		packet.length = bos.size();

		PacketDispatcher.sendPacketToServer(packet);
	}

	/*public static Packet packetFromTileEntity(TileEntity te) {
		NBTTagCompound tag = new NBTTagCompound();
		te.writeToNBT(tag);
		return packetFromNBT(0, tag);
	}

	public static Packet packetFromNBT(int type, NBTTagCompound tag) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream(bytes);
		try {
			stream.write(type);
			NBTBase.writeNamedTag(tag, stream);
		}
		catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
		byte[] data = bytes.toByteArray();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "dynamicTanks";
		packet.length = data.length;
		packet.data = data;
		return packet;
	}

	public static NBTTagCompound nbtFromPacket(Packet250CustomPayload pkt) {
		DataInput stream = new DataInputStream(new ByteArrayInputStream(pkt.data, 1, pkt.length - 1));
		try {
			NBTBase tag = NBTBase.readNamedTag(stream);
			return (NBTTagCompound)tag;
		}
		catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
	}*/

}
