package com.theburgerclub.burgercredit.domain.mapper

import com.theburgerclub.burgercredit.data.local.entity.DishEntity
import com.theburgerclub.burgercredit.domain.model.Dish

fun DishEntity.toDomain(): Dish = Dish(
    id = id,
    name = name,
    price = price,
    photoUri = photoUri
)

fun Dish.toEntity(): DishEntity = DishEntity(
    id = id,
    name = name,
    price = price,
    photoUri = photoUri
) 