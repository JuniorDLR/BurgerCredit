package com.theburgerclub.burgercredit.domain.mapper

import com.theburgerclub.burgercredit.data.local.entity.AdminEntity
import com.theburgerclub.burgercredit.domain.model.Admin

object AdminMapper {

    fun toAdmin(adminEntity: AdminEntity): Admin {
        return Admin(
            id = adminEntity.id,
            username = adminEntity.username,
            password = adminEntity.password
        )
    }

    fun toAdminEntity(admin: Admin): AdminEntity {
        return AdminEntity(
            id = admin.id,
            username = admin.username,
            password = admin.password
        )
    }

    fun toAdminList(adminEntityList: List<AdminEntity>): List<Admin> {
        return adminEntityList.map { toAdmin(it) }
    }

    fun toAdminEntityList(adminList: List<Admin>): List<AdminEntity> {
        return adminList.map { toAdminEntity(it) }
    }
}