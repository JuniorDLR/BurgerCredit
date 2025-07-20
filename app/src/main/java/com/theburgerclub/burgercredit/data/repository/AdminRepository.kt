package com.theburgerclub.burgercredit.data.repository

import com.theburgerclub.burgercredit.data.local.dao.AdminDao
import com.theburgerclub.burgercredit.domain.mapper.AdminMapper
import com.theburgerclub.burgercredit.domain.model.Admin
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val adminDao: AdminDao
) {
    
    suspend fun getAdmin(): Admin? {
        val adminEntity = adminDao.getAdmin()
        return adminEntity?.let { AdminMapper.toAdmin(it) }
    }
    
    suspend fun authenticateAdmin(username: String, password: String): Admin? {
        val adminEntity = adminDao.authenticateAdmin(username, password)
        return adminEntity?.let { AdminMapper.toAdmin(it) }
    }
    
    suspend fun checkUsernameExists(username: String): Boolean {
        return adminDao.checkUsernameExists(username) > 0
    }
    
    suspend fun createAdmin(admin: Admin): Long {
        val adminEntity = AdminMapper.toAdminEntity(admin)
        return adminDao.insertAdmin(adminEntity)
    }
    
    suspend fun updateAdmin(admin: Admin) {
        val adminEntity = AdminMapper.toAdminEntity(admin)
        adminDao.updateAdmin(adminEntity)
    }
    
    suspend fun deleteAllAdmins() {
        adminDao.deleteAllAdmins()
    }
} 