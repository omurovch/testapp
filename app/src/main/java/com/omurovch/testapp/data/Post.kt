package com.omurovch.testapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Post(
    @PrimaryKey(autoGenerate = true) val postId: Int,
    val title: String,
    val author: String,
    val publishedAt: Date = Date(),
    val show: Boolean = false
) {
    override fun toString() = "$title - $author - $publishedAt"
}
