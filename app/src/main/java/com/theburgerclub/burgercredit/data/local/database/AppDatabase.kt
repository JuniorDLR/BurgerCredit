package com.theburgerclub.burgercredit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theburgerclub.burgercredit.data.local.dao.CustomerDao
import com.theburgerclub.burgercredit.data.local.dao.DishDao
import com.theburgerclub.burgercredit.data.local.dao.DebtDao
import com.theburgerclub.burgercredit.data.local.dao.AdminDao
import com.theburgerclub.burgercredit.data.local.entity.CustomerEntity
import com.theburgerclub.burgercredit.data.local.entity.DishEntity
import com.theburgerclub.burgercredit.data.local.entity.DebtEntity
import com.theburgerclub.burgercredit.data.local.entity.AdminEntity

@Database(
    entities = [CustomerEntity::class, DishEntity::class, DebtEntity::class, AdminEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun dishDao(): DishDao
    abstract fun debtDao(): DebtDao
    abstract fun adminDao(): AdminDao
} 