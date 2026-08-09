package de.donbarz;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public class Quest {

    QuestTarget target;
    int progress;
    int amountGoal;
    Component questText;

    static Codec<Quest> CODEC = QuestType.REGISTRY.byNameCodec()
            .dispatch("type", Quest::getType, QuestType::codec);

    QuestType<?> getType() {return null;}
}
