package com.omurovch.testapp

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.omurovch.testapp.data.Post

class PostAdapter : PagedListAdapter<Post, PostViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(parent)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.postId == newItem.postId

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.author == newItem.author && oldItem.title == newItem.title && oldItem.publishedAt == newItem.publishedAt
        }
    }
}