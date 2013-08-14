package doc.dynamictanks;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import com.google.common.collect.Lists;

import doc.dynamictanks.block.BlockManager;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

public class CoreChunkloadCallback implements ForgeChunkManager.OrderedLoadingCallback {

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for (Ticket ticket : tickets) {
			int quarryX = ticket.getModData().getInteger("CoreX");
			int quarryY = ticket.getModData().getInteger("CoreY");
			int quarryZ = ticket.getModData().getInteger("CoreZ");
			TileEntityMultiTankCore tileCore = (TileEntityMultiTankCore) world.getBlockTileEntity(quarryX, quarryY, quarryZ);
			tileCore.forceChunkLoading(ticket);

		}
	}

	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
		List<Ticket> validTickets = Lists.newArrayList();
		for (Ticket ticket : tickets) {
			int quarryX = ticket.getModData().getInteger("CoreX");
			int quarryY = ticket.getModData().getInteger("CoreY");
			int quarryZ = ticket.getModData().getInteger("CoreZ");

			TileEntity teChk = world.getBlockTileEntity(quarryX, quarryY, quarryZ);
			if (teChk instanceof TileEntityMultiTankCore) {
				validTickets.add(ticket);
			}
		}
		return validTickets;
	}
	
}