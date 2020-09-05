package com.kotlin.ekstremeat.Common

import com.kotlin.ekstremeat.Model.CategoryModel
import com.kotlin.ekstremeat.Model.UserModel

object Common {
    var categorySelected:CategoryModel?=null
    val CATEGORY_REF: String = "Category"
    val FULL_WIDTH_COLUMN: Int =1
    val DEFAULT_COLUMN_COUNT: Int = 0
    val BEST_DEALS_REF :String = "BestDeals"
    val POPULAR_REF: String ="MostPopular"
    val USER_REFERENCE = "Users"
    var currentUser:UserModel?=null
}