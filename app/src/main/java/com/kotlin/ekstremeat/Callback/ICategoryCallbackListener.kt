package com.kotlin.ekstremeat.Callback

import com.kotlin.ekstremeat.Model.CategoryModel

interface ICategoryCallbackListener {
    fun onCategoryLoadSuccess(categoriesList:List<CategoryModel>)
    fun onCategoryLoadFailed(message:String)
}