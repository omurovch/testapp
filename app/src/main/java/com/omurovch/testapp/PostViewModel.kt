package com.omurovch.testapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.paging.Config
import androidx.paging.toLiveData
import com.omurovch.testapp.data.Post
import com.omurovch.testapp.data.PostDatabase
import com.omurovch.testapp.helper.ioThread
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class PostViewModel(app: Application) : AndroidViewModel(app) {

    private val postDao = PostDatabase.getInstance(app).postDao()
    private var addingService: ScheduledFuture<*>? = null

    val refreshDone = MutableLiveData<Boolean>()

    val allPosts = postDao.posts().toLiveData(Config(pageSize = 20, enablePlaceholders = true, maxSize = 200))

    val newPostsCount = postDao.newPostsCount()

    fun startAddingService() {
        addingService = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            val start = postDao.maxPostId() + 1
            postDao.insert((start until start + 5).map {
                Post(
                    postId = 0,
                    author = "Author",
                    title = "Post"
                )
            })
        }, 0, 1, TimeUnit.SECONDS)
    }

    fun showNewPosts() {
        ioThread {
            postDao.showNewPosts()
            refreshDone.postValue(true)
        }
    }

    fun deletePosts() {
        ioThread {
            postDao.deletePosts()
        }
    }

    override fun onCleared() {
        addingService?.cancel(true)
    }
}