package doc.dynamictanks.client.gui.slots;

import doc.dynamictanks.items.ItemManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class chipsetSlot extends Slot {

	public chipsetSlot(IInventory inv, int par1, int par2, int par3)
    {
        super(inv, par1, par2, par3);
    }

    @Override
    public boolean isItemValid(ItemStack is)
    {
        if(is.itemID == ItemManager.chipSet.itemID)
            return true;

        return false;
    }
}
