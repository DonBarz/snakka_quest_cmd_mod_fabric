package de.donbarz;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

// theres different quest types for different targets (eg. blocks mined/ entities killed)
public record QuestType<T extends Quest>(MapCodec<T> codec) {
    public static final Registry<QuestType<?>> REGISTRY = new MappedRegistry<>(
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(SnacksQuestMod.MOD_ID, "quest_types")), Lifecycle.stable());

}
