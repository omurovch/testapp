package com.omurovch.testapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.omurovch.testapp.data.Post
import java.text.SimpleDateFormat
import java.util.*

class PostViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.post_view_holder, parent, false)
) {
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val author = itemView.findViewById<TextView>(R.id.author)
    private val date = itemView.findViewById<TextView>(R.id.date)

    fun bind(post: Post?) {
        title.text = post?.let { it.title + " " + it.postId }
        author.text = post?.let { it.author + " " + it.postId }
        date.text = post?.let { SimpleDateFormat("MMMM d, YYYY HH:mm", Locale.getDefault()).format(post.publishedAt) }
    }
}