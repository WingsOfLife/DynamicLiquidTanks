package doc.dynamictanks.Utils;

import doc.dynamictanks.block.BlockManager;
import net.minecraft.world.World;

public class BlockUtils {

	public static void checkAndUpdateComparator(World worldObj, int xCoord, int yCoord, int zCoord) {
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, BlockManager.tankCore.blockID);
	}
	
}
