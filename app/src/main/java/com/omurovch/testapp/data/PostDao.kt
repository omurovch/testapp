package com.omurovch.testapp.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PostDao {

    @Query("SELECT max(postId) FROM Post")
    fun maxPostId(): Int

    @Query("SELECT * FROM Post where show = 1 ORDER BY postId DESC")
    fun posts(): DataSource.Factory<Int, Post>

    @Query("SELECT COUNT(*) FROM Post where show = 0")
    fun newPostsCount(): LiveData<Int>

    @Query("UPDATE Post set show = 1 where show = 0")
    fun showNewPosts()

    @Insert
    fun insert(cheeses: List<Post>)

    @Query("DELETE FROM Post")
    fun deletePosts()
}