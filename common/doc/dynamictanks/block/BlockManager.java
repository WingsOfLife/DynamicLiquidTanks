package doc.dynamictanks.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import doc.dynamictanks.DynamicTanks;
import doc.dynamictanks.common.ModConfig;
import doc.dynamictanks.items.TankCoreBlockItem;

public class BlockManager
{
	public static Block tankCore = null;
	public static Block tankSub = null;
	public static Block tankDimension = null;
	public static Block potionBlock = null;

	public static void registerBlocks()
	{
		tankCore = new BlockTankCore(ModConfig.BlockIDs.tankCore);
		tankSub = new BlockTankSub(ModConfig.BlockIDs.tankSub);
		tankDimension = new BlockTankDimExt(ModConfig.BlockIDs.tankDimentsion);
		potionBlock = new BlockPotion(ModConfig.BlockIDs.potion, DynamicTanks.fluidPotion, Material.water);

		GameRegistry.registerBlock(tankCore, TankCoreBlockItem.class, "blockTankCore");
		GameRegistry.registerBlock(tankSub, "blockTankSub");
		GameRegistry.registerBlock(tankDimension, "blockTankDimension");
		GameRegistry.registerBlock(potionBlock, "liquidPotion");

		LanguageRegistry.addName(tankCore, "Multi-Tank Core");
		LanguageRegistry.addName(tankSub, "Multi-Tank Extension");
		LanguageRegistry.addName(tankDimension, "Dimension Extension");
		LanguageRegistry.addName(potionBlock, "Liquid Potion");
	}

	public static void registerCraftingRecipes()
	{
		if (ModConfig.BooleanVars.oldRecipes == false) {
			CraftingManager.getInstance().addRecipe(new ItemStack(tankSub, 4),
					"GCG",
					"CPC",
					"GCG", 'G', Block.glass, 'C', Item.flowerPot, 'P', Item.bucketEmpty);

			CraftingManager.getInstance().addRecipe(new ItemStack(tankCore, 2),
					"TCT",
					"CIC",
					"TCT", 'T', tankSub, 'C', Item.bucketEmpty, 'I', Item.ingotGold);
		} else {
			CraftingManager.getInstance().addRecipe(new ItemStack(tankSub, 4),
					"GCG",
					"CPC",
					"GCG", 'G', Block.glass, 'C', Item.flowerPot, 'P', Item.ingotIron);

			CraftingManager.getInstance().addRecipe(new ItemStack(tankCore, 2),
					"TCT",
					"CIC",
					"TCT", 'T', tankSub, 'C', Item.flowerPot, 'I', Item.ingotGold);
		}

		CraftingManager.getInstance().addRecipe(new ItemStack(tankDimension, 1),
				"EOE",
				"GCG",
				"EOE", 'E', Item.eyeOfEnder, 'C', tankCore, 'G', Item.ghastTear, 'O', Block.obsidian);
	}
}
