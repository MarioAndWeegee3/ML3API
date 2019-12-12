package marioandweegee3.ml3api.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Allows an Item to specify a custom remainder, outside of vanilla restrictions.
 * 
 * Should only be implemented on a class that extends {@link net.minecraft.item.Item}
 */
public interface CustomRemainder {

    /**
     * Returns an {@link ItemStack} to replace the vanilla remainder.
     * Modifying the input stack is safe; it is a copy.
     * @param stack The {@link ItemStack} that is normally returned
     * @param player The {@link PlayerEntity} that used this in a crafting recipe
     * @return An {@link ItemStack} that replaces the vanilla remainder.
     */
    public ItemStack getRemainder(ItemStack stack, PlayerEntity player);
}