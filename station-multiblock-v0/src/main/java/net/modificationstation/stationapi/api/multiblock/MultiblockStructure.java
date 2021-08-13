package net.modificationstation.stationapi.api.multiblock;

import lombok.Getter;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.Vec3i;
import net.minecraft.util.maths.Box;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiblockStructure {
    private final Map<Vec3i, MultiblockPart> structure = new HashMap<>();
    private final List<String[]> layers = new ArrayList<>();
    private final Map<@NotNull Character, @NotNull ItemInstance> blockMap;
    private final BlockBase controllerBlock;
    @Getter private Box boundingBox;

    public MultiblockStructure(@NotNull BlockBase controllerBlock, @NotNull Map<@NotNull Character, @NotNull ItemInstance> blockMap) {
        this.controllerBlock = controllerBlock;
        blockMap.values().forEach((itemInstance) -> {
            if (itemInstance.getType().id >= BlockBase.BY_ID.length) {
                throw new IllegalArgumentException("Specified item is not a block!");
            }
        });

        List<Character> keyList = new ArrayList<>();
        blockMap.keySet().forEach((key) -> {
            if (keyList.contains(key)) {
                throw new IllegalArgumentException("Duplicate keys in structure!");
            }
            keyList.add(key);
        });
        this.blockMap = blockMap;
    }

    public MultiblockStructure addLayer(@NotNull String[] rows) {
        layers.add(rows);
        return this;
    }

    public void build() {
        boolean hasController = false;
        Vec3i controllerPos = null;

        for (int y = 0; y < layers.size(); y++) { // y
            String[] layer = layers.get(y);
            for (int z = 0; z < layer.length; z++) { // z
                char[] row = layer[y].toCharArray();
                for (int x = 0; x < row.length; x++) { // x
                    char character = row[x];
                    if (BlockBase.BY_ID[blockMap.get(character).itemId] == controllerBlock) {
                        if (hasController) {
                            throw new IllegalArgumentException("Multiple controllers in the structure!");
                        }
                        hasController = true;
                        controllerPos = new Vec3i(x, y, z);
                    }
                }
            }
        }
        if (!hasController) {
            throw new IllegalArgumentException("No controllers in structure!");
        }

        for (int y = 0; y < layers.size(); y++) { // y
            String[] layer = layers.get(y);
            for (int z = 0; z < layer.length; z++) { // z
                char[] row = layer[y].toCharArray();
                for (int x = 0; x < row.length; x++) { // x
                    char character = row[x];
                    structure.put(new Vec3i(x - controllerPos.x, y - controllerPos.y, z - controllerPos.z), new MultiblockPart(blockMap.get(character), BlockBase.BY_ID[blockMap.get(character).itemId] == controllerBlock));
                }
            }
        }

        boundingBox = Box.create(-controllerPos.x, -controllerPos.y, -controllerPos.z, layers.get(0)[0].length() - controllerPos.x, layers.size() - controllerPos.y, layers.get(0).length - controllerPos.z);
    }

}
