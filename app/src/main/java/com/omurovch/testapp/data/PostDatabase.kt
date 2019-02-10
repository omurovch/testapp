package com.omurovch.testapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.omurovch.testapp.helper.ioThread

@Database(entities = [Post::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {

        @Volatile
        private var instance: PostDatabase? = null

        fun getInstance(context: Context): PostDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PostDatabase {
            return Room.databaseBuilder(context, PostDatabase::class.java, "dbTestApp")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        insertInitialPosts(context.applicationContext)
                    }
                })
                .build()
        }

        private fun insertInitialPosts(context: Context) {
            ioThread {
                getInstance(context).postDao().insert(
                    (1..100).map { Post(postId = 0, author = "Author", title = "Post", show = true) })
            }
        }
    }
}
