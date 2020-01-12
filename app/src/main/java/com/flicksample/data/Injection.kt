package com.flicksample.data

import com.flicksample.data.network.ForumService
import com.flicksample.data.network.HttpBuilder
import com.flicksample.data.network.interactor.SearchPhotoInteractorImpl
import com.flicksample.data.network.interactor.SearchPhotosInteractor
import com.flicksample.ui.main.MainContract
import com.flicksample.ui.main.MainPresenter

object Injection {
    private lateinit var searchPhotoInteractor: SearchPhotosInteractor
    private lateinit var service: ForumService
    private lateinit var presenter: MainContract.Presenter

    fun getSearchPhotoInteractor(): SearchPhotosInteractor {
        if (!::searchPhotoInteractor.isInitialized) {
            searchPhotoInteractor = SearchPhotoInteractorImpl(getForumService())
        }
        return searchPhotoInteractor
    }

    fun getForumService(): ForumService {
        if (!::service.isInitialized) {
            service = HttpBuilder().initRetrofit()
        }
        return service
    }

    fun getMainPresenter(view: MainContract.View): MainContract.Presenter {
        if (!::presenter.isInitialized) {
            presenter = MainPresenter(
                view,
                getSearchPhotoInteractor()
            )
        }
        return presenter
    }
}