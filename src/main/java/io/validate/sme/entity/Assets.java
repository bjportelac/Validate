package io.validate.sme.entity;

/**
 * Represents the assets of an entity.
 * This record is used to store information about whether the entity has assets
 * and the total value of those assets.
 *
 * @param hasAssets   Indicates if the entity possesses any assets.
 * @param assetsValue The total monetary value of the assets.
 */
public record Assets(boolean hasAssets, double assetsValue) {
}

