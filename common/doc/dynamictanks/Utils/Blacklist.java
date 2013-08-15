package doc.dynamictanks.Utils;

import net.minecraft.block.Block;

public class Blacklist {

	public final static int[] blackList = { Block.dirt.blockID, Block.grass.blockID, Block.cobblestone.blockID, Block.obsidian.blockID };
	
	public static boolean isBlacklist(int itemID) {
		for (int i : blackList) {
			if (i == itemID) {
				return false;
			}
		}
		return true;
	}
	
}
