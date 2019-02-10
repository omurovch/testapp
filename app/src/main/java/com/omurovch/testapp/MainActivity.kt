package com.omurovch.testapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.omurovch.testapp.helper.ioThread
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)

        val adapter = PostAdapter()
        postList.adapter = adapter
        postList.layoutManager = LinearLayoutManager(this)

        viewModel.allPosts.observe(this, Observer(adapter::submitList))

        viewModel.newPostsCount.observe(this, Observer { newPostsCount ->
            if (newPostsCount == 0) {
                newPostsButton.visibility = View.GONE
                postList.postDelayed({ postList.smoothScrollToPosition(0) }, 250)
            } else {
                newPostsButton.visibility = View.VISIBLE
                newPostsButton.text = "$newPostsCount new posts"
            }
        })

        newPostsButton.setOnClickListener {
            viewModel.showNewPosts()
        }

        pullToRefresh.setProgressViewOffset(
            false,
            resources.getDimensionPixelSize(R.dimen.refresher_offset_start),
            resources.getDimensionPixelSize(R.dimen.refresher_offset_end)
        )

        pullToRefresh.setOnRefreshListener {
            viewModel.showNewPosts()
        }

        viewModel.refreshDone.observe(this, Observer {
            pullToRefresh.isRefreshing = false
        })

        reloadButton.setOnClickListener {
            postsPage.visibility = View.VISIBLE
            errorPage.visibility = View.GONE
            ioThread {
                Thread.sleep(1000)
                viewModel.startAddingService()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.clear_menu, menu)
        super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.clear_button) {
            viewModel.deletePosts()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}
