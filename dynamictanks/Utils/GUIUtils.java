package doc.dynamictanks.Utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import doc.dynamictanks.DynamicTanks;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

public class GUIUtils {
	
	public static String getFluidName(TileEntityMultiTankCore tileEntity) {
		if (TEUtil.getBottomTankLiquid(tileEntity) != null && TEUtil.getBottomTankLiquid(tileEntity).getFluid() == DynamicTanks.fluidPotion) {
			ItemStack getName = new ItemStack(Item.potion, 1, tileEntity.storedPotionId);
			return StringUtils.Cap(StringUtils.lastName(getName.getDisplayName()));
		}
		if (TEUtil.getBottomTankLiquid(tileEntity) != null) 
			try {
				return "Liquid: " + StringUtils.Cap(FluidRegistry.getFluidName(TEUtil.getBottomTankLiquid(tileEntity)));
			} catch (Exception e) {
				return "Liquid: Empty";
			}
		return "Liquid: Empty";
	}
	
	public static String getFluidFromStack(FluidStack stack) {
		if (stack.fluidID > 0) 
			try {
				return "Liquid: " + StringUtils.Cap(FluidRegistry.getFluidName(stack));
			} catch (Exception e) {
				return "Liquid: Empty";
			}
		return "Liquid: Empty";
	}
	
}
