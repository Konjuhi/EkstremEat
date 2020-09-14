package com.kotlin.ekstremeat.ui.fooddetail

import android.app.AlertDialog
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.andremion.counterfab.CounterFab
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.kotlin.ekstremeat.Common.Common
import com.kotlin.ekstremeat.Model.CommentModel
import com.kotlin.ekstremeat.Model.FoodModel
import com.kotlin.ekstremeat.R
import com.kotlin.ekstremeat.ui.comment.CommentFragment
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_food_detail.*
import org.w3c.dom.Text
import java.lang.StringBuilder

class FoodDetailFragment : Fragment() {

    private lateinit var foodDetailViewModel: FoodDetailViewModel

    private var img_food: ImageView?=null
    private var btnCart: CounterFab?=null
    private var btnRating:FloatingActionButton?=null
    private var food_name:TextView?=null
    private var food_description:TextView?=null
    private var food_price:TextView? = null
    private var number_button:ElegantNumberButton?=null
    private var ratingBar:RatingBar?=null
    private var btnShowComment: Button?=null

    private var waitingAlertDialog:AlertDialog?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        foodDetailViewModel =
            ViewModelProviders.of(this).get(FoodDetailViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_food_detail, container, false)

        initViews(root)

        foodDetailViewModel.getMutableLiveDataFood().observe(viewLifecycleOwner, Observer {
            displayInfo(it)
        })

        foodDetailViewModel.getMutableLiveDataComment().observe(this, Observer {
            submitRatingToFirebase(it)
        })

        return root
    }

    private fun submitRatingToFirebase(it: CommentModel?) {
        waitingAlertDialog!!.show()

        //First, we will submit to Comment Ref

        FirebaseDatabase.getInstance().getReference(Common.COMMENT_REF)
            .child(Common.foodSelected!!.id!!)
            .push()
            .setValue(it)
            .addOnCompleteListener{task ->
                if(task.isSuccessful)
                {

                    addRatingFood(it!!.ratingValue)
                }
                waitingAlertDialog!!.dismiss()
            }


    }

    private fun addRatingFood(ratingValue: Float) {

        FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REF)//Select category
            .child(Common.categorySelected!!.menu_id!!)//Select Menu in category
            .child("foods")//Select foods array
            .child(Common.foodSelected!!.key!!)//Select Key
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        val foodModel = dataSnapshot.getValue(FoodModel::class.java)
                        foodModel!!.key = Common.foodSelected!!.key

                        //Apply rating
                        val sumRating = foodModel.ratingValue+ratingValue
                        val ratingCount = foodModel.ratingCount+1
                        val result = sumRating/ratingCount

                        val update = HashMap<String,Any>()
                        update["ratingValue"] = result
                        update["ratingCount"] = ratingCount

                        //Update data in variable

                        foodModel.ratingCount = ratingCount
                        foodModel.ratingValue = result

                        dataSnapshot.ref
                            .updateChildren(update)
                            .addOnCompleteListener{task ->
                                waitingAlertDialog!!.dismiss()
                                if(task.isSuccessful)
                                {
                                    Common.foodSelected = foodModel
                                    foodDetailViewModel!!.setFoodModel(foodModel)
                                    Toast.makeText(context!!,"Thank you",Toast.LENGTH_SHORT).show()
                                }
                            }

                    }else{
                        waitingAlertDialog!!.dismiss()
                    }

                }

                override fun onCancelled(p0: DatabaseError) {
                   waitingAlertDialog!!.dismiss()
                    Toast.makeText(context!!,""+p0.message,Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun displayInfo(it: FoodModel?) {
      Glide.with(context!!).load(it!!.image).into(img_food!!)
        food_name!!.text = StringBuilder(it!!.name!!)
        food_description!!.text = StringBuilder(it!!.description!!)
        food_price!!.text = StringBuilder(it!!.price.toString())

        ratingBar!!.rating = it!!.ratingValue.toFloat()

    }

    private fun initViews(root: View?) {
        waitingAlertDialog = SpotsDialog.Builder().setContext(context!!).setCancelable(false).build()

        btnCart = root!!.findViewById(R.id.btnCart) as CounterFab
        img_food = root!!.findViewById(R.id.img_food) as ImageView
        btnRating = root!!.findViewById(R.id.btn_rating) as FloatingActionButton
        food_name = root!!.findViewById(R.id.food_name) as TextView
        food_description = root!!.findViewById(R.id.food_description) as TextView
        food_price = root!!.findViewById(R.id.food_price )as TextView
        number_button = root!!.findViewById(R.id.number_button) as ElegantNumberButton
        ratingBar = root!!.findViewById(R.id.ratingBar)  as RatingBar
        btnShowComment = root!!.findViewById(R.id.btnShowComment) as Button

        //Event
        btnRating!!.setOnClickListener{
            showDialogRating()
        }

        btnShowComment!!.setOnClickListener{
            val commentFragment = CommentFragment.getInstance()
            commentFragment.show(activity!!.supportFragmentManager,"CommentFragment")
        }


    }

    private fun showDialogRating() {
        var builder = AlertDialog.Builder(context!!)
        builder.setTitle("Rating Food")
        builder.setMessage("Please fill information")

        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_rating_comment,null)

        var ratingBar = itemView.findViewById<RatingBar>(R.id.rating_bar)
        var edt_comment = itemView.findViewById<EditText>(R.id.edt_comment)

        builder.setView(itemView)

        builder.setNegativeButton("Cancel"){
            dialogInterface, i-> dialogInterface.dismiss()}

        builder.setPositiveButton("OK"){ dialogInterface, i->
            val commentModel = CommentModel()
            commentModel.name = Common.currentUser!!.name
            commentModel.uid = Common.currentUser!!.uid
            commentModel.comment = edt_comment.text.toString()
            commentModel.ratingValue = ratingBar.rating

            val serverTimeStamp = HashMap<String,Any>()
            serverTimeStamp["timeStamp"] = ServerValue.TIMESTAMP
            commentModel.commentTimeStamp=(serverTimeStamp)

            foodDetailViewModel!!.setCommentModel(commentModel)
        }
        val dialog = builder.create()
        dialog.show()

        }
    }

