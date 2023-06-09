package com.example.lostandfoundete.activities.Lost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfoundete.Adapters.MyAdapter
import com.example.lostandfoundete.R
import com.example.lostandfoundete.DataClass.Things
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LostFeedActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noDataImageView: ImageView
    private lateinit var noDataTextView: TextView
    private lateinit var thingsList: ArrayList<Things>
    private var db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
//
        requestWindowFeature(Window.FEATURE_NO_TITLE) //will hide the title
        supportActionBar?.hide() //hide the title bar

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_feed)

        recyclerView = findViewById(R.id.recycler_view_lost_feed)
        noDataImageView = findViewById(R.id.no_data_imageView)
        noDataTextView = findViewById(R.id.no_data_textView)
        recyclerView.layoutManager = LinearLayoutManager(this@LostFeedActivity)

        thingsList = arrayListOf()

        fetchData()


    }

    private fun fetchData() {
        if(thingsList.isEmpty()){
            noDataImageView.visibility = View.VISIBLE
            noDataTextView.visibility = View.VISIBLE
        }
        db = FirebaseFirestore.getInstance()
        db.collection("Lost Items").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents) {
//                    db.collection("user").document()
                    val thing: Things? = data.toObject(Things::class.java)
                    if (thing != null) {
                        thingsList.add(thing)
                        noDataImageView.visibility = View.INVISIBLE
                        noDataTextView.visibility = View.INVISIBLE
                    }
                }
                recyclerView.adapter = MyAdapter(this, thingsList)
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }




    }
}