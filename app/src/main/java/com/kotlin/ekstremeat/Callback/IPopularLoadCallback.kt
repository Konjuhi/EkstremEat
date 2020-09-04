package com.kotlin.ekstremeat.Callback

import com.kotlin.ekstremeat.Model.PopularCategoryModel

interface IPopularLoadCallback {

    fun onPopularLoadSuccess(popularModelList:List<PopularCategoryModel>)
    fun onPopularLoadFailed(message:String)
}