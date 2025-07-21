package com.theburgerclub.burgercredit.data.repository

import com.theburgerclub.burgercredit.data.local.dao.DishDao
import com.theburgerclub.burgercredit.data.local.entity.DishEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishRepository @Inject constructor(
    private val dishDao: DishDao
) {
    suspend fun insertDish(dish: DishEntity): Long =
        dishDao.insertDish(dish)

    suspend fun updateDish(dish: DishEntity) =
        dishDao.updateDish(dish)

    suspend fun deleteDish(dish: DishEntity) =
        dishDao.deleteDish(dish)

    fun getAllDishes(): Flow<List<DishEntity>> =
        dishDao.getAllDishes()

    suspend fun getDishById(id: Long): DishEntity? =
        dishDao.getDishById(id)

    fun searchDishesByName(name: String): Flow<List<DishEntity>> =
        dishDao.searchDishesByName(name)

    suspend fun getDishByName(name: String): DishEntity? =
        dishDao.getDishByName(name)
} 