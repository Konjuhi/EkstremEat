package com.kotlin.ekstremeat.ui.foodList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.ekstremeat.Common.Common
import com.kotlin.ekstremeat.Adapter.MyFoodListAdapter

import com.kotlin.ekstremeat.R


/**
 * A simple [Fragment] subclass.
 */
class FoodListFragment : Fragment() {

    private lateinit var foodListViewModel:FoodListViewModel

    var recycler_food_list : RecyclerView?=null
    var layoutAnimationController:LayoutAnimationController?=null

    var adapter : MyFoodListAdapter?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        foodListViewModel =ViewModelProviders.of(this).get(FoodListViewModel::class.java)

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_food_list, container, false)

        initView(root)

        foodListViewModel.getMutableFoodModelListData().observe(this,Observer{
            adapter = MyFoodListAdapter(context!!,it)
            recycler_food_list!!.adapter = adapter
            recycler_food_list!!.layoutAnimation = layoutAnimationController

        })
        return root
    }

    private fun initView(root: View?) {

        recycler_food_list = root!!.findViewById(R.id.recycler_food_list)as RecyclerView
        recycler_food_list!!.setHasFixedSize(true)
        recycler_food_list!!.layoutManager = LinearLayoutManager(context)

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_item_from_left)

        (activity as AppCompatActivity).supportActionBar!!.title = Common.categorySelected!!.name

    }
}
