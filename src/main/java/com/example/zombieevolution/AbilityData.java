package com.example.zombieevolution;

public interface AbilityData {
    int getAbilityFlags();
    void setAbilityFlags(int flags);

    default boolean hasAbility(int ability) {
        return (getAbilityFlags() & ability) != 0;
    }

    default void addAbility(int ability) {
        setAbilityFlags(getAbilityFlags() | ability);
    }
}
