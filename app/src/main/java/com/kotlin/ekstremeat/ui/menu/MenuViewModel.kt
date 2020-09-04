package com.kotlin.ekstremeat.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlin.ekstremeat.Callback.ICategoryCallbackListener
import com.kotlin.ekstremeat.Common.Common
import com.kotlin.ekstremeat.Model.CategoryModel
import com.kotlin.ekstremeat.Model.PopularCategoryModel
import java.util.*
import kotlin.collections.ArrayList

class MenuViewModel : ViewModel(), ICategoryCallbackListener {

   private var categoriesListMutable: MutableLiveData<List<CategoryModel>>? =null
    private var messageError:MutableLiveData<String> = MutableLiveData()
    private val categoryCallBackListener : ICategoryCallbackListener

    init {
        categoryCallBackListener = this
    }

    override fun onCategoryLoadSuccess(categoriesList: List<CategoryModel>) {
        categoriesListMutable!!.value= categoriesList
    }

    override fun onCategoryLoadFailed(message: String) {
        messageError.value = message
    }

    fun getCategoryList(): MutableLiveData<List<CategoryModel>> {
        if(categoriesListMutable == null){
            categoriesListMutable = MutableLiveData()
            loadCategory()
        }
        return categoriesListMutable!!
    }

    fun getMessageErro():MutableLiveData<String>{
        return messageError
    }

    private fun loadCategory() {
        val tempList = ArrayList<CategoryModel>()
        val categoryRef = FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REF)
        categoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (itemSnapshot in p0!!.children){
                    val model = itemSnapshot.getValue<CategoryModel>(CategoryModel::class.java)
                    model!!.menu_id = itemSnapshot.key
                    tempList.add(model!!)
                }
                categoryCallBackListener.onCategoryLoadSuccess(tempList)
            }

            override fun onCancelled(p0: DatabaseError) {
                categoryCallBackListener.onCategoryLoadFailed(p0.message!!)
            }


        })


    }
}