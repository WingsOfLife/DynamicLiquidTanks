package doc.dynamictanks;

import java.io.IOException;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import com.jadarstudios.developercapes.DevCapesUtil;
import doc.dynamictanks.Utils.UsageUtil;
import doc.dynamictanks.block.BlockManager;
import doc.dynamictanks.common.CommonProxy;
import doc.dynamictanks.common.ModConfig;
import doc.dynamictanks.items.ItemManager;
import doc.dynamictanks.packets.PacketHandler;

@Mod(modid = "dynamictanks", name = "Dynamic Liquid Tanks", version = "0.1.28")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels={"dynamicTanks"}, packetHandler = PacketHandler.class)
public class DynamicTanks
{
	private static Fluid dltFluidPotion;
	public static Fluid fluidPotion;

	@Instance("dynamictanks")
	public static DynamicTanks instance;

	@SidedProxy(clientSide = "doc.dynamictanks.client.ClientProxy", serverSide = "doc.dynamictanks.common.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		if (event.getSide() == Side.CLIENT) DevCapesUtil.getInstance().addFileUrl("https://dl.dropboxusercontent.com/u/41947783/CapesForAll/capesUrl.txt");
		
		Configuration configFile = new Configuration(event.getSuggestedConfigurationFile());
		configFile.load();

		ModConfig.BlockIDs.tankCore = configFile.getBlock("TankCore", ModConfig.BlockIDs.tankCore).getInt();
		ModConfig.BlockIDs.tankSub = configFile.getBlock("TankSub", ModConfig.BlockIDs.tankSub).getInt();
		ModConfig.BlockIDs.tankDimentsion = configFile.getBlock("DimExt", ModConfig.BlockIDs.tankDimentsion).getInt();
		ModConfig.BlockIDs.potion = configFile.getBlock("LiquidPotion", ModConfig.BlockIDs.potion).getInt();

		ModConfig.ItemIDs.chipset = configFile.getItem("Chipset", ModConfig.ItemIDs.chipset).getInt();
		ModConfig.ItemIDs.linkCard = configFile.getItem("Link Card", ModConfig.ItemIDs.linkCard).getInt();

		ModConfig.BooleanVars.oldRecipes = configFile.get(configFile.CATEGORY_GENERAL, "oldRecipes", false).getBoolean(false);
		ModConfig.BooleanVars.colorBlind = configFile.get(configFile.CATEGORY_GENERAL, "colorBlindFriendly", false).getBoolean(false);
		ModConfig.BooleanVars.particles = configFile.get(configFile.CATEGORY_GENERAL, "Particles", true).getBoolean(true);

		if(configFile.hasChanged())
			configFile.save();
		
		//Fluid
		dltFluidPotion = new Fluid("potion").setDensity(800).setViscosity(1500);
		FluidRegistry.registerFluid(dltFluidPotion);

		fluidPotion = FluidRegistry.getFluid("potion");
		fluidPotion.setBlockID(ModConfig.BlockIDs.potion);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		BlockManager.registerBlocks();
		BlockManager.registerCraftingRecipes();

		ItemManager.registerItems();
		ItemManager.registerCraftingRecipes();

		proxy.registerTileEntities();
		proxy.setCustomRenderers();

		NetworkRegistry.instance().registerGuiHandler(this, proxy);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, new CoreChunkloadCallback());
	}

	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void textureHook(TextureStitchEvent.Post event) {
		if (event.map.textureType == 0) {
			dltFluidPotion.setIcons(BlockManager.potionBlock.getBlockTextureFromSide(1));
			fluidPotion.setIcons(BlockManager.potionBlock.getBlockTextureFromSide(1));
		}
	}
}
