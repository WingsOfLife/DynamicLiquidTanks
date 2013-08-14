package doc.dynamictanks.Utils;

import doc.dynamictanks.block.BlockManager;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class BlockUtils {

	public static void checkAndUpdateComparator(World worldObj, int xCoord, int yCoord, int zCoord) {
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, BlockManager.tankCore.blockID);
	}
	
}
