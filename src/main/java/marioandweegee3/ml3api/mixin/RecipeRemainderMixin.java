package marioandweegee3.ml3api.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import marioandweegee3.ml3api.util.CustomRemainder;
import net.minecraft.container.CraftingResultSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

@Mixin(CraftingResultSlot.class)
public abstract class RecipeRemainderMixin{

    @Shadow
    @Final
    private CraftingInventory craftingInv;

    @Shadow 
    @Final 
    private PlayerEntity player;

    @ModifyVariable(at = @At(value = "INVOKE"), method = "onTakeItem", ordinal = 0)
    private DefaultedList<ItemStack> modifyRemainders(DefaultedList<ItemStack> list){
        for(int i = 0; i < craftingInv.getInvSize(); i++){
            ItemStack ing = craftingInv.getInvStack(i);
            if(ing.getItem() instanceof CustomRemainder){
                CustomRemainder rem = (CustomRemainder)ing.getItem();
                ItemStack remainder = rem.getRemainder(ing.copy(), player);
                list.set(i, remainder);
            }
        }

        return list;
    }
}