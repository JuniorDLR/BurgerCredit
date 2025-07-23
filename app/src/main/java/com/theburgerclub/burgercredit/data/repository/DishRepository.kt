package com.theburgerclub.burgercredit.data.repository

import com.theburgerclub.burgercredit.data.local.dao.DishDao
import com.theburgerclub.burgercredit.data.local.entity.DishEntity
import kotlinx.coroutines.flow.Flow
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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

    suspend fun getDishByName(name: String): DishEntity? =
        dishDao.getDishByName(name)

    fun getDishesPaging(pageSize: Int = 10): Flow<PagingData<DishEntity>> =
        Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { dishDao.pagingSource() }
        ).flow

    fun searchDishesPaging(name: String, pageSize: Int = 10): Flow<PagingData<DishEntity>> =
        Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { dishDao.pagingSourceByName(name) }
        ).flow

    suspend fun getDishCount(): Int = dishDao.getDishCount()
} 