package com.peasantrebellion.model.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Rectangle

class BodyComponent(
    var body: Rectangle,
    val onCollision: (thisEntity: Entity, otherEntity: Entity) -> Unit = { _, _ -> },
    val entitiesToCollideWith: Family = Family.all().get(),
) : Component
