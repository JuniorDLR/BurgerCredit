package com.theburgerclub.burgercredit.domain.usecase

import com.theburgerclub.burgercredit.data.repository.DishRepository
import com.theburgerclub.burgercredit.domain.mapper.toDomain
import com.theburgerclub.burgercredit.domain.mapper.toEntity
import com.theburgerclub.burgercredit.domain.model.Dish
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DishUseCase @Inject constructor(
    private val repository: DishRepository
) {
    suspend fun addDish(dish: Dish): Long =
        repository.insertDish(dish.toEntity())

    suspend fun updateDish(dish: Dish) =
        repository.updateDish(dish.toEntity())

    suspend fun deleteDish(dish: Dish) =
        repository.deleteDish(dish.toEntity())

    fun getAllDishes(): Flow<List<Dish>> =
        repository.getAllDishes().map { list -> list.map { it.toDomain() } }

    suspend fun getDishById(id: Long): Dish? =
        repository.getDishById(id)?.toDomain()

    fun searchDishesByName(name: String): Flow<List<Dish>> =
        repository.searchDishesByName(name).map { list -> list.map { it.toDomain() } }
} 