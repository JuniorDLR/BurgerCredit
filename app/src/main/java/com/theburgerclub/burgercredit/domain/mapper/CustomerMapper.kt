package com.theburgerclub.burgercredit.domain.mapper

import com.theburgerclub.burgercredit.data.local.entity.CustomerEntity
import com.theburgerclub.burgercredit.domain.model.Customer

fun CustomerEntity.toDomain(): Customer = Customer(
    id = id,
    name = name
)

fun Customer.toEntity(): CustomerEntity = CustomerEntity(
    id = id,
    name = name
) 