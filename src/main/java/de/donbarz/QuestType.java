package de.donbarz;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public record QuestType<T extends Quest>(MapCodec<T> codec) {
    public static final Registry<QuestType<?>> REGISTRY = new MappedRegistry<>(
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(SnacksQuestMod.MOD_ID, "bean_types")), Lifecycle.stable());
}
