package doc.dynamictanks.client.gui.slots;

import doc.dynamictanks.items.ItemManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class fuelSlot extends Slot
{
    private EntityPlayer thePlayer;

    public fuelSlot(EntityPlayer par1EntityPlayer, IInventory par2IInventory, int par3, int par4, int par5)
    {
        super(par2IInventory, par3, par4, par5);
        this.thePlayer = par1EntityPlayer;
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getItem() == Item.enderPearl)
        	return true;
        return false;
    }
}