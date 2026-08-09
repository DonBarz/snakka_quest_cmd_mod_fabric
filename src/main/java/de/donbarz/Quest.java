package de.donbarz;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

abstract class Quest {

    QuestTarget target;
    int progress;
    int amountGoal;
    Component questText;

    static Codec<Quest> CODEC = QuestType.REGISTRY.byNameCodec()
            .dispatch("type", Quest::getType, QuestType::codec);

    abstract QuestType<?> getType();
}
