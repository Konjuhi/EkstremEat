package com.kotlin.ekstremeat.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.ekstremeat.Callback.IRecyclerItemClickListener
import com.kotlin.ekstremeat.Common.Common
import com.kotlin.ekstremeat.EventBus.CategoryClick
import com.kotlin.ekstremeat.Model.CategoryModel
import com.kotlin.ekstremeat.R
import org.greenrobot.eventbus.EventBus

class MyCategoriesAdapter (internal var context: Context,internal var categoriesList:List<CategoryModel>):
RecyclerView.Adapter<MyCategoriesAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var category_name:TextView? =null
        var category_image:ImageView? = null

       internal var listener:IRecyclerItemClickListener?=null

        fun setListener(listener: IRecyclerItemClickListener)
        {
            this.listener = listener
        }

        init {
            category_name = itemView.findViewById(R.id.category_name) as TextView
            category_image = itemView.findViewById(R.id.category_image) as ImageView
            itemView.setOnClickListener(this)

        }

        override fun onClick(view: View?) {
            listener!!.onItemClick(view!!,adapterPosition)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
         return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(categoriesList.get(position).image).into(holder.category_image!!)
        holder.category_name!!.text= categoriesList[position].name

        //Event
        holder.setListener(object :IRecyclerItemClickListener{
            override fun onItemClick(view: View, pos: Int) {
                Common.categorySelected = categoriesList[pos]
                EventBus.getDefault().postSticky(CategoryClick(true,categoriesList.get(pos)))
            }
        })
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(categoriesList.size == 1)
            Common.DEFAULT_COLUMN_COUNT
        else{
            if(categoriesList.size % 2 ==0)
                Common.DEFAULT_COLUMN_COUNT
            else
                if(position > 1 && position == categoriesList.size-1) Common.FULL_WIDTH_COLUMN else Common.DEFAULT_COLUMN_COUNT
        }
    }
}


