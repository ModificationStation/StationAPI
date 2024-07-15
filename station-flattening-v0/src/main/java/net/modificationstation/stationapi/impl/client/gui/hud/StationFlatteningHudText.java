package net.modificationstation.stationapi.impl.client.gui.hud;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.event.gui.hud.HudTextRenderEvent;
import net.modificationstation.stationapi.api.client.event.gui.hud.HudTextLine;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.tag.TagKey;

import java.util.Collection;
import java.util.Collections;

import static net.modificationstation.stationapi.api.client.event.gui.hud.HudTextLine.GRAY;
import static net.modificationstation.stationapi.api.client.event.gui.hud.HudTextLine.WHITE;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationFlatteningHudText {
    @EventListener(phase = StationAPI.INTERNAL_PHASE)
    private static void renderHudText(HudTextRenderEvent event) {
        if (!event.debug) return;
        HitResult hit = event.minecraft.field_2823;
        if (hit == null || hit.type != HitResultType.BLOCK) return;
        BlockState state = event.minecraft.world.getBlockState(hit.blockX, hit.blockY, hit.blockZ);
        Collections.addAll(event.right,
                new HudTextLine("Block: " + state.getBlock().getTranslatedName(), WHITE, 20),
                new HudTextLine("Meta: " + event.minecraft.world.getBlockMeta(hit.blockX, hit.blockY, hit.blockZ))
        );
        Collection<Property<?>> properties = state.getProperties();
        if (!properties.isEmpty()) {
            event.right.add(new HudTextLine("Properties:"));
            event.right.addAll(properties.stream().map(prop ->
                    new HudTextLine(prop.getName() + ": " + state.get(prop), GRAY)
            ).toList());
        }

        Collection<TagKey<Block>> tags = state.streamTags().toList();
        if (!tags.isEmpty()) {
            event.right.add(new HudTextLine("Tags:"));
            event.right.addAll(tags.stream().map(tag -> new HudTextLine("#" + tag.id(), GRAY)).toList());
        }

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            BlockEntity entity = event.minecraft.world.method_1777(hit.blockX, hit.blockY, hit.blockZ);
            if (entity != null) {
                String className = entity.getClass().getName();
                String text = "Tile Entity: " + className.substring(className.lastIndexOf('.') + 1);
                event.right.add(new HudTextLine(text, WHITE, 20));
            }
        }
    }
}