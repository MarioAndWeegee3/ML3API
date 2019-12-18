package marioandweegee3.ml3api.registry;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Helps with registering Items
 */
public final class RegistryHelper {
    private final String modid;

    public final Logger logger;

    /**
     * Initialize a new RegistryHelper
     * @param modid The mod's id. Can be any string that works in {@link Identifier}
     */
    public RegistryHelper(String modid){
        this.modid = modid;
        this.logger = LogManager.getLogger(modid);
    }

    /**
     * Logs the message with the included {@link Logger}
     * @param message The message to log
     */
    public void log(String message){
        logger.info("["+modid+"] "+message);
    }

    /**
     * @return The mod id passed in the constructor
     */
    public String getMod(){
        return modid;
    }

    /**
     * Makes an {@link Identifier} for the given string, using the modid
     * @param name The path for the new {@link Identifier}
     * @return An {@link Identifier} with the given path and {@link #modid} as the namespace
     */
    public Identifier makeId(String name){
        return new Identifier(modid, name);
    }

    /**
     * Registers all {@link Item}s in the given {@link Map}
     * @param map A map of the item names and {@link Item}s
     */
    public void registerAllItems(Map<String, Item> map){
        for(Map.Entry<String, Item> entry : map.entrySet()){
            registerItem(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Registers all {@link Block}s in the given {@link Map}
     * @param map A map of the item names and {@link Block}s
     * @param group The {@link ItemGroup} used when registering the {@link BlockItem}s
     */
    public void registerAllBlocks(Map<String, Block> map, ItemGroup group){
        for(Map.Entry<String, Block> entry : map.entrySet()){
            registerBlock(entry.getKey(), entry.getValue(), group);
        }
    }

    @Deprecated
    public void register(String name, Object object){
        if(object instanceof Item){
            registerItem(name, (Item) object);
        } else if(object instanceof BlockEntityType){
            registerBlockEntity(name, (BlockEntityType<?>) object);
        }
    }

    @Deprecated
    public void register(String name, Object object, ItemGroup group){
        if(object instanceof Block) {
            registerBlock(name, (Block) object, group);
        }
    }

    @Deprecated
    public void registerAll(Map<String, Item> map){
        registerAllItems(map);
    }

    @Deprecated
    public void registerAll(Map<String, Block> map, ItemGroup group) {
        registerAllBlocks(map, group);
    }

    /**
     * Registers the given {@link Item} in the appropriate {@link Registry}
     * @param name The name of the {@link Item}
     * @param item The {@link Item}
     */
    public void registerItem(String name, Item item){
        Registry.ITEM.add(makeId(name), item);
    }

    /**
     * Registers the given {@link Block} in the appropriate {@link Registry}
     * @param name The name of the {@link Block}
     * @param block The {@link Block}
     * @param group The {@link ItemGroup} used when registering the {@link BlockItem}
     */
    public void registerBlock(String name, Block block, ItemGroup group){
        registerItem(name, new BlockItem(block, new Item.Settings().group(group)));
        Registry.BLOCK.add(makeId(name), block);
    }

    /**
     * Registers the given {@link BlockEntityType} in the appropriate {@link Registry}
     * @param name The name of the {@link BlockEntityType}
     * @param type The {@link BlockEntityType}
     */
    public void registerBlockEntity(String name, BlockEntityType<?> type){
        Registry.register(Registry.BLOCK_ENTITY, makeId(name), type);
    }
}