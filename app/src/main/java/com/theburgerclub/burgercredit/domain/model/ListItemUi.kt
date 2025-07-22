package com.theburgerclub.burgercredit.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

interface ListItemUi {
    fun getTitle(): String
    fun getSubtitle(): String?
    fun getIcon(): ImageVector
} 