package com.flicksample.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.flicksample.R
import com.flicksample.data.Injection
import com.flicksample.model.Photo
import com.flicksample.ui.base.EndlessRecyclerAdapter
import com.flicksample.ui.main.adapter.PhotoListAdapter
import com.flicksample.ui.main.adapter.PhotoListener
import com.flicksample.util.FlickrApi
import com.flicksample.util.SearchQueryListener
import com.flicksample.views.search.MaterialSearchView
import kotlinx.android.synthetic.main.view_list_photos.*

class MainActivity : FullScreenPagerActivity(), MainContract.View, PhotoListener {
    private val presenter = Injection.getMainPresenter(this)
    private val adapter by lazy { PhotoListAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActionBar()
        initRecyclerView()
        initSearchView()
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        adapter.setLoadingOffset(FlickrApi.PAGE_SIZE / 2)
        adapter.setCallbacks(scrollListener)
        recyclerView.adapter = adapter
    }

    private fun initSearchView() {
        searchView.setOnQueryTextChangeListener(object : SearchQueryListener(lifecycle) {
            override fun onQueryChanged(newText: String?) {
                presenter.query(newText)
            }
        })
        searchView.viewDismissListener = object : MaterialSearchView.OnViewClosedListener {
            override fun onDismiss() {
                onBackPressed()
            }
        }
    }

    override fun setSearchActive(isActive: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isActive)
    }

    override fun onClickPhoto(position: Int) {
        if (searchView.isExpanded()) {
            searchView.collapseSearch()
        }
        onExpandImage(position)
    }

    private val scrollListener = object : EndlessRecyclerAdapter.LoaderCallbacks {

        override fun canLoadNextItems(): Boolean = adapter.hasMore

        override fun loadNextItems() {
            val count = adapter.getCount() + FlickrApi.PAGE_SIZE
            presenter.getPhotos(count)
        }
    }

    override fun onSuccess(result: List<Photo>, hasNext: Boolean) {
        val onBottom = recyclerView.findViewHolderForAdapterPosition(adapter.getCount() - 1) != null
        if (onBottom) {
            recyclerView.stopScroll()
        }

        adapter.setPhotos(result, hasNext)
        setPhotos(result)
        adapter.onNextItemsLoaded()
    }

    override fun onError() {
        adapter.onNextItemsError()
    }

    override fun onBackPressed() {
        when {
            searchView.isExpanded() -> {
                presenter.disableSearch()
                searchView.collapseSearch()
            }
            presenter.isSearchActive() -> presenter.disableSearch()
            else -> super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                searchView.expandSearch()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showProgress() {
        loadingProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        loadingProgress.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}