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

public class RegistryHelper{
    private final String modid;

    public final Logger logger;

    public RegistryHelper(String modid){
        this.modid = modid;
        this.logger = LogManager.getLogger(modid);
    }

    public void log(String message){
        logger.info(message);
    }

    public String getMod(){
        return modid;
    }

    public Identifier makeId(String name){
        return new Identifier(modid, name);
    }

    public void registerAll(Map<String, Item> map){
        for(Map.Entry<String, Item> entry : map.entrySet()){
            register(entry.getKey(), entry.getValue());
        }
    }

    public void registerAll(Map<String, Block> map, ItemGroup group){
        for(Map.Entry<String, Block> entry : map.entrySet()){
            register(entry.getKey(), entry.getValue(), group);
        }
    }

    public void register(String name, Item item){
        Registry.ITEM.add(makeId(name), item);
    }

    public void register(String name, Block block, ItemGroup group){
        register(name, new BlockItem(block, new Item.Settings().group(group)));
        Registry.BLOCK.add(makeId(name), block);
    }

    public void register(String name, BlockEntityType<?> type){
        Registry.register(Registry.BLOCK_ENTITY, makeId(name), type);
    }
}