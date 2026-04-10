package net.modificationstation.stationapi.mixin.flattening.client.stats;

import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.stat.PlayerStats;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.registry.StatRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.stat.ModdedStatsSerializingIterator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(PlayerStats.class)
class PlayerStatsMixin {
    @Inject(
            method = "deserialize",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/util/MD5MessageDigest;"
            )
    )
    private static void stationapi_deserializeModdedStats(
            String data, CallbackInfoReturnable<Map<Stat, Integer>> cir,
            @Local(index = 1) HashMap<Stat, Integer> statCounts, @Local(index = 4) JsonRootNode statsRoot
    ) {
        //noinspection unchecked
        for (final JsonNode node : (List<JsonNode>) statsRoot.getArrayNode(ModdedStatsSerializingIterator.STATIONAPI_STATS_CHANGE_KEY)) {
            @SuppressWarnings("unchecked") final Map<JsonStringNode, JsonNode> statCountNodes = node.getFields();
            final Map.Entry<JsonStringNode, JsonNode> statCountNode = statCountNodes.entrySet().iterator().next();
            final Identifier statId = Identifier.tryParse(statCountNode.getKey().getText());
            final Stat stat = StatRegistry.INSTANCE.get(statId);
            final int count = Integer.parseInt(statCountNode.getValue().getText());
            if (stat == null)
                System.out.println(statId + " is not a valid stat");
            else
                statCounts.put(stat, count);
        }
    }

    @WrapOperation(
            method = "serialize",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
            )
    )
    private static Iterator<Stat> stationapi_filterOutModdedStats(
            Set<Stat> instance, Operation<Iterator<Stat>> original,
            @Local(index = 2, argsOnly = true) Map<Stat, Integer> statCounts,
            @Share("moddedStats") LocalRef<StringBuilder> moddedStatsRef
    ) {
        final StringBuilder moddedStats = new StringBuilder();
        moddedStatsRef.set(moddedStats);

        return new ModdedStatsSerializingIterator(original.call(instance), moddedStats, statCounts);
    }

    @Inject(
            method = "serialize",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/StringBuilder;append(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                    ordinal = 17,
                    shift = At.Shift.AFTER
            )
    )
    private static void stationapi_appendModdedStats(
            String playerName, String sessionId, Map<Stat, Integer> stats, CallbackInfoReturnable<String> cir,
            @Local(index = 3) StringBuilder statsJson,
            @Share("moddedStats") LocalRef<StringBuilder> moddedStatsRef
    ) {
        statsJson.append(moddedStatsRef.get());
    }
}
