package com.theburgerclub.burgercredit.presentation.dishes.model

sealed class ImageError {
    object None : ImageError()
    object Empty : ImageError()
    object ErrorSize : ImageError()
    object ErrorExtension : ImageError()
} 