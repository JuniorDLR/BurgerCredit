package com.theburgerclub.burgercredit.domain.usecase

import com.theburgerclub.burgercredit.data.repository.DishRepository
import com.theburgerclub.burgercredit.domain.mapper.toDomain
import com.theburgerclub.burgercredit.domain.mapper.toEntity
import com.theburgerclub.burgercredit.domain.model.Dish
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.paging.PagingData
import androidx.paging.map
import com.theburgerclub.burgercredit.data.local.entity.DishEntity
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

    suspend fun getDishByName(name: String): Dish? {
        return repository.getDishByName(name)?.toDomain()
    }

    fun getDishesPaging(pageSize: Int = 10): Flow<PagingData<Dish>> =
        repository.getDishesPaging(pageSize).map { pagingData: PagingData<DishEntity> ->
            pagingData.map { it.toDomain() }
        }

    fun searchDishesPaging(name: String, pageSize: Int = 10): Flow<PagingData<Dish>> =
        repository.searchDishesPaging(name, pageSize).map { pagingData: PagingData<DishEntity> ->
            pagingData.map { it.toDomain() }
        }

    suspend fun getDishCount(): Int = repository.getDishCount()
} 