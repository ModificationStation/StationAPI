package net.modificationstation.stationapi.api.client.event.block.entity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import java.util.Map;

@SuperBuilder
public class BlockEntityRendererRegisterEvent extends Event {
    public final Map<Class<? extends BlockEntity>, BlockEntityRenderer> renderers;
}
