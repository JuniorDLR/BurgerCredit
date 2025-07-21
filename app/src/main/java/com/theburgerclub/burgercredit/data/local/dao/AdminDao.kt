package com.theburgerclub.burgercredit.data.local.dao

import androidx.room.*
import com.theburgerclub.burgercredit.data.local.entity.AdminEntity

@Dao
interface AdminDao {
    
    @Query("SELECT * FROM admin LIMIT 1")
    suspend fun getAdmin(): AdminEntity?
    
    @Query("SELECT * FROM admin WHERE username = :username AND password = :password LIMIT 1")
    suspend fun authenticateAdmin(username: String, password: String): AdminEntity?
    
    @Query("SELECT COUNT(*) FROM admin WHERE username = :username")
    suspend fun checkUsernameExists(username: String): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminEntity): Long
    
    @Update
    suspend fun updateAdmin(admin: AdminEntity)
    
    @Query("DELETE FROM admin")
    suspend fun deleteAllAdmins()
} 