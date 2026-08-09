package de.donbarz;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public abstract class Quest {

    protected QuestTarget target;
    protected int progress;
    protected int amountGoal;
    protected Component questText;

    protected Quest(QuestTarget target, int amountGoal, int progress, Component questText) {
        this.target = target;
        this.amountGoal = amountGoal;
        this.progress = progress;
        this.questText = questText;
    }

    public static final Codec<Quest> CODEC = QuestType.REGISTRY.byNameCodec()
            .dispatch("type", Quest::getType, QuestType::codec);

    public QuestTarget getTarget() {
        return target;
    }

    public int getProgress() {
        return progress;
    }

    public int getAmountGoal() {
        return amountGoal;
    }

    public Component getQuestText() {
        return questText;
    }

    public abstract QuestType<?> getType();
}
