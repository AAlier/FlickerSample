package com.flicksample.views.search

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import com.flicksample.R
import com.flicksample.data.SearchQueryCacheManager
import com.flicksample.util.*
import kotlinx.android.synthetic.main.search_view.view.*

class MaterialSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr
) {
    private val view by lazy { inflater<View>(this, R.layout.search_view, true) }
    private val rootView: LinearLayout by lazy { view.rootLayout }
    private val listView: ListView by lazy { view.listView }
    private val searchBarView: LinearLayout by lazy { view.searchBarView }
    private val backButton: ImageView by lazy { view.backButton }
    private val closeButton: ImageView by lazy { view.clearButton }
    private val searchEditText: AppCompatEditText by lazy { view.searchEditText }
    private var listener: SearchQueryListener? = null
    private var isSearchExpanded = false
    private lateinit var adapter: Adapter
    private var listViewAnimator: ViewPropertyAnimator? = null
    var viewDismissListener: OnViewClosedListener? = null

    override fun clearFocus() {
        super.clearFocus()
        hideKeyboard()
    }

    fun expandSearch() {
        if (isSearchExpanded) return
        setSuggestions(SearchQueryCacheManager.getQuery())
        listViewAnimator?.cancel()
        listViewAnimator = listView.animate().alpha(1f).setDuration(250)
        listViewAnimator!!.start()
        searchEditText.setText("")
        searchEditText.requestFocus()

        rootView.visibility = View.VISIBLE
        AnimationUtils.circleRevealView(searchBarView)
        searchEditText.removeTextChangedListener(editTextListener)
        searchEditText.addTextChangedListener(editTextListener)
        isSearchExpanded = true
    }

    fun collapseSearch() {
        if (!isSearchExpanded) return
        listViewAnimator?.cancel()
        listViewAnimator = listView.animate().alpha(0f).setDuration(250)
        listViewAnimator!!.start()
        clearFocus()
        searchEditText.removeTextChangedListener(editTextListener)
        searchEditText.setText("")
        val adapter = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                rootView.visibility = View.GONE
            }
        }

        AnimationUtils.circleHideView(searchBarView, adapter)
        isSearchExpanded = false
    }

    fun setOnQueryTextChangeListener(listener: SearchQueryListener) {
        this.listener = listener
    }

    fun isExpanded() = isSearchExpanded

    private fun setupViews() {
        backButton.setOnDelayedClickListener {
            collapseSearch()
            viewDismissListener?.onDismiss()
        }
        closeButton.setOnDelayedClickListener {
            searchEditText.text = null
        }
        adapter = Adapter(context)
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            onSubmitQuery(adapter.getItem(position) as String)
        }
        setupSearchInput()
    }

    private fun setSuggestions(list: Set<String>) {
        if (::adapter.isInitialized) {
            adapter.setData(list)
        }
    }

    private fun setupSearchInput() {
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val text = searchEditText.text.toString().trim()
            onSubmitQuery(text)
        }

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showKeyboard(searchEditText)
            }
        }
    }

    private fun onSubmitQuery(text: String): Boolean {
        if (text.isNotEmpty()) {
            listener?.onQueryTextChange(text)
            collapseSearch()
            SearchQueryCacheManager.addQuery(text)
        }

        return true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupViews()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listViewAnimator?.cancel()
    }

    private val editTextListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            listener?.onQueryTextChange(text.toString())
            adapter.filter.filter(text)
        }

    }

    interface OnViewClosedListener {
        fun onDismiss()
    }
}