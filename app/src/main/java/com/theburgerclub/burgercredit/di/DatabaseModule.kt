package com.theburgerclub.burgercredit.di

import android.content.Context
import androidx.room.Room
import com.theburgerclub.burgercredit.data.local.database.AppDatabase
import com.theburgerclub.burgercredit.data.local.dao.CustomerDao
import com.theburgerclub.burgercredit.data.local.dao.DishDao
import com.theburgerclub.burgercredit.data.local.dao.DebtDao
import com.theburgerclub.burgercredit.data.local.dao.AdminDao
import com.theburgerclub.burgercredit.data.local.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "burger_credit_db"
        ).build()

    @Provides
    fun provideCustomerDao(db: AppDatabase): CustomerDao = db.customerDao()

    @Provides
    fun provideDishDao(db: AppDatabase): DishDao = db.dishDao()

    @Provides
    fun provideDebtDao(db: AppDatabase): DebtDao = db.debtDao()

    @Provides
    fun provideAdminDao(db: AppDatabase): AdminDao = db.adminDao()

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext appContext: Context): UserPreferences = UserPreferences(appContext)
} 