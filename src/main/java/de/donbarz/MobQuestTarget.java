package de.donbarz;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

public class MobQuestTarget extends QuestTarget{
    private final EntityType<?> target;

    public static final Codec<MobQuestTarget> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("target").forGetter(MobQuestTarget::get_target)
    ).apply(instance, MobQuestTarget::new));

    public MobQuestTarget (EntityType<?> target) {
        this.target = target;
    }

    @Override
    Object statTarget() {
        return target;
    }

    @Override
    Component name() {
        return Component.translatable(target.getDescriptionId());
    }

    public EntityType<?> get_target () {
        return target;
    }
}