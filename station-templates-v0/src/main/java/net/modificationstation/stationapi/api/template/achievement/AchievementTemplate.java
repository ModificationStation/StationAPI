package net.modificationstation.stationapi.api.template.achievement;

import net.minecraft.achievement.Achievement;
import net.modificationstation.stationapi.api.registry.StatRegistry;
import net.modificationstation.stationapi.api.template.stat.StatTemplate;
import net.modificationstation.stationapi.api.util.Identifier;

public interface AchievementTemplate extends StatTemplate {
    static int getNextId() {
        return StatRegistry.SHIFTED_ACHIEVEMENT_ID.get(StatTemplate.getNextId());
    }

    static void onConstructor(Achievement achievement, Identifier id) {
        StatTemplate.onConstructor(achievement, id);
    }
}
