package com.rizrmdhn.core.common

object Helpers {
    fun avatarGenerator(username: String): String {
        return "https://ui-avatars.com/api/?name=$username&length=1&background=random&size=128"
    }
}