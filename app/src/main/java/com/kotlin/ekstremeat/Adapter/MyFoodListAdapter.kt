package com.kotlin.ekstremeat.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.ekstremeat.Callback.IRecyclerItemClickListener
import com.kotlin.ekstremeat.Common.Common
import com.kotlin.ekstremeat.EventBus.FoodItemClick
import com.kotlin.ekstremeat.Model.FoodModel
import com.kotlin.ekstremeat.R
import org.greenrobot.eventbus.EventBus

class MyFoodListAdapter (internal var context: Context,internal var foodList:List<FoodModel>):
RecyclerView.Adapter<MyFoodListAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_food_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(foodList[position].image).into(holder.img_food_image!!)
        holder.txt_food_name!!.text = foodList[position].name
        holder.txt_food_price!!.text=foodList[position].price.toString()

        //Event
        holder.setListener(object:IRecyclerItemClickListener{
            override fun onItemClick(view: View, pos: Int) {
                Common.foodSelected = foodList[pos]
                Common.foodSelected!!.key = pos.toString()
                EventBus.getDefault().postSticky(FoodItemClick(true,foodList[pos]))
            }

        })
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var txt_food_name:TextView?=null
        var txt_food_price:TextView?=null

        var img_food_image:ImageView?=null
        var img_fav:ImageView?=null
        var img_cart:ImageView?=null

        internal var listener: IRecyclerItemClickListener?=null


        fun setListener(listener: IRecyclerItemClickListener)
        {
            this.listener = listener
        }

        init {
            txt_food_name = itemView.findViewById(R.id.txt_food_name) as TextView
            txt_food_price = itemView.findViewById(R.id.txt_food_price) as TextView
            img_food_image = itemView.findViewById(R.id.img_food_image) as ImageView
            img_cart = itemView.findViewById(R.id.img_quick_cart) as ImageView
            img_fav = itemView.findViewById(R.id.img_fav) as ImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            listener!!.onItemClick(view!!,adapterPosition)

        }

    }
}