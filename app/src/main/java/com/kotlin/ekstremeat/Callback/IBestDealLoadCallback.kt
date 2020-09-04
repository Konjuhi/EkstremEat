package com.kotlin.ekstremeat.Callback

import com.kotlin.ekstremeat.Model.BestDealModel

interface IBestDealLoadCallback {
    fun onBestDealLoadSuccess(bestDealList:List<BestDealModel>)
    fun onBestDealLoadFailed(message:String)
}