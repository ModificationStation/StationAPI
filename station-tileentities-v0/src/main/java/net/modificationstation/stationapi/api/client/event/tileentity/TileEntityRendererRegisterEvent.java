package net.modificationstation.stationapi.api.client.event.tileentity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import java.util.Map;

@SuperBuilder
public class TileEntityRendererRegisterEvent extends Event {
    public final Map<Class<? extends BlockEntity>, BlockEntityRenderer> renderers;
}
