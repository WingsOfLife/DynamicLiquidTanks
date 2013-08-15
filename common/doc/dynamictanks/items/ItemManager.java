package doc.dynamictanks.items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import doc.dynamictanks.common.ModConfig;

public class ItemManager {

    public static int outputChipMeta = 7;

	public static Item chipSet = null;
	public static Item linkCard = null;
	
	public static void registerItems() {
		
		linkCard = new LinkCard(ModConfig.ItemIDs.linkCard);
		LanguageRegistry.addName(linkCard, "Link Card");
		
		chipSet = new Chipset(ModConfig.ItemIDs.chipset);
		for (int i = 0; i < Chipset.names.length; i++) {
			LanguageRegistry.addName(new ItemStack(chipSet, 1, i), Chipset.names[i] + " Chipset");
		}
		
	}
	
	public static void registerCraftingRecipes() {
		
		GameRegistry.addRecipe(new ItemStack(chipSet, 1, 0),
				new Object[] { " Q ", "CRC", " P ",
			'C', Item.coal, 'Q', Item.diamond, 'R', Item.glowstone, 'P', Block.blockRedstone			
		});
		
		GameRegistry.addRecipe(new ItemStack(chipSet, 1, 1),
				new Object[] { " Q ", "CRC", " P ",
			'C', Block.blockIron, 'Q', Item.diamond, 'R', Item.glowstone, 'P', Block.blockRedstone			
		});
		
		GameRegistry.addRecipe(new ItemStack(chipSet, 1, 2),
				new Object[] { " Q ", "CRC", " P ",
			'C', Block.blockGold, 'Q', Item.diamond, 'R', Item.glowstone, 'P', Block.blockRedstone			
		});
		
		GameRegistry.addRecipe(new ItemStack(chipSet, 1, 3),
				new Object[] { " Q ", "CRC", " P ",
			'C', Block.blockLapis, 'Q', Item.diamond, 'R', Item.glowstone, 'P', Block.blockRedstone			
		});
		
		GameRegistry.addRecipe(new ItemStack(chipSet, 1, 4),
				new Object[] { " Q ", "CRC", " P ",
			'C', Block.blockDiamond, 'Q', Item.diamond, 'R', Item.glowstone, 'P', Block.blockRedstone			
		});
		
		GameRegistry.addRecipe(new ItemStack(chipSet, 1, 5),
				new Object[] { " Q ", "CRC", " P ",
			'C', Block.blockEmerald, 'Q', Item.diamond, 'R', Item.glowstone, 'P', Block.blockRedstone			
		});
		
		GameRegistry.addRecipe(new ItemStack(chipSet, 1, 6),
				new Object[] { " Q ", "CRC", " P ",
			'C', Item.netherStar, 'Q', Item.diamond, 'R', Item.glowstone, 'P', Block.blockRedstone			
		});
		
		GameRegistry.addRecipe(new ItemStack(linkCard, 1, 0),
				new Object[] { "III", "PGP", "III",
			'I', Item.ingotIron, 'P', Item.enderPearl, 'G', Item.ingotGold,		
		});
		
		GameRegistry.addShapelessRecipe(new ItemStack(linkCard, 1, 0), 
				new Object[] { 
			linkCard 
		});
		
	}
	
}
