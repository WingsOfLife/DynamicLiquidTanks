package doc.dynamictanks.Utils;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;

public class collisionEffects {

	public static void applyPotionEffects(EntityPlayer player, int potionId, boolean override) {

		List list = PotionHelper.getPotionEffects(potionId, override);

		if (list != null)
		{
			Iterator iterator = list.iterator();

			while (iterator.hasNext())
			{
				PotionEffect potioneffect = (PotionEffect)iterator.next();
				potioneffect.duration = potioneffect.getDuration() / 10;
				player.addPotionEffect(new PotionEffect(potioneffect));
			}
		}
	}

}
