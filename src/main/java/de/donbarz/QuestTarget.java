package de.donbarz;

import com.mojang.serialization.Codec;

abstract class QuestTarget {
    Object target;
    abstract String name();
}
