package io.validate.sme.entity;

/**
 * Represents the assets of an entity.
 * This record is used to store information about whether the entity has assets
 * and the total value of those assets.
 *
 * @param assetsValue The total monetary value of the assets.
 */
public record Assets(double assetsValue) {

    @Override
    public String toString() {
        return "Assets: { assetsValue=" + assetsValue + '}';
    }
}

