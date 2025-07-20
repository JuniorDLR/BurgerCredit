package com.theburgerclub.burgercredit.domain.usecase

import com.theburgerclub.burgercredit.data.repository.AdminRepository
import com.theburgerclub.burgercredit.domain.model.Admin
import javax.inject.Inject

class AdminUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    
    suspend fun getAdmin(): Admin? {
        return adminRepository.getAdmin()
    }
    
    suspend fun authenticateAdmin(username: String, password: String): Admin? {
        return adminRepository.authenticateAdmin(username, password)
    }
    
    suspend fun checkUsernameExists(username: String): Boolean {
        return adminRepository.checkUsernameExists(username)
    }
    
    suspend fun createAdmin(admin: Admin): Long {
        return adminRepository.createAdmin(admin)
    }
    
    suspend fun updateAdmin(admin: Admin) {
        adminRepository.updateAdmin(admin)
    }
} 