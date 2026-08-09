package de.donbarz;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.EntityType;

public class MobQuestTarget extends QuestTarget{
    private final EntityType<?> target;

    public static final Codec<MobQuestTarget> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            // Up to 16 fields can be declared here
            EntityType.CODEC.fieldOf("target").forGetter(MobQuestTarget::get_target)
    ).apply(instance, MobQuestTarget::new));

    public MobQuestTarget (EntityType<?> target) {
        this.target = target;
    }

    @Override
    Object statTarget() {
        return target;
    }

    @Override
    String name() {
        return target.toShortString();
    }

    public EntityType<?> get_target () {
        return target;
    }
}