package com.kotlin.ekstremeat.ui.foodList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.ekstremeat.Common.Common
import com.kotlin.ekstremeat.Model.FoodModel

class FoodListViewModel :ViewModel(){
    private var mutableFoodListData:MutableLiveData<List<FoodModel>>?=null

    fun getMutableFoodModelListData(): MutableLiveData<List<FoodModel>>{
        if(mutableFoodListData == null)
            mutableFoodListData = MutableLiveData()
        mutableFoodListData!!.value = Common.categorySelected!!.foods
        return mutableFoodListData!!
    }
}