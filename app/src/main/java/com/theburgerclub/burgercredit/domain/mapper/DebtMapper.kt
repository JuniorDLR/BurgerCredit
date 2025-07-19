package com.theburgerclub.burgercredit.domain.mapper

import com.theburgerclub.burgercredit.data.local.entity.DebtEntity
import com.theburgerclub.burgercredit.domain.model.Debt

fun DebtEntity.toDomain(): Debt = Debt(
    id = id,
    customerId = customerId,
    amount = amount,
    dueDate = dueDate,
    isActive = isActive,
    description = description
)

fun Debt.toEntity(): DebtEntity = DebtEntity(
    id = id,
    customerId = customerId,
    amount = amount,
    dueDate = dueDate,
    isActive = isActive,
    description = description
) 