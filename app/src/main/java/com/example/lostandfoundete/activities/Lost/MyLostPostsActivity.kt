package com.example.lostandfoundete.activities.Lost

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfoundete.Adapters.MyLostItemsAdapter
import com.example.lostandfoundete.R
import com.example.lostandfoundete.DataClass.Things
import com.example.lostandfoundete.activities.Found.MyFoundPosts
import com.example.lostandfoundete.activities.ProfilePageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyLostPostsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var thingsList: ArrayList<Things>
    private lateinit var thingsIdList: ArrayList<String>
    private lateinit var FoundItemsBtn : Button
    private lateinit var HomeBtn : Button
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_lost_posts)

        FoundItemsBtn = findViewById(R.id.my_found_posts)
        HomeBtn = findViewById(R.id.my_lost_home_button)

        FoundItemsBtn.setOnClickListener {
            val intent = Intent(this, MyFoundPosts::class.java)
            startActivity(intent)
            finish()
        }
        HomeBtn.setOnClickListener {
            val intent = Intent(this@MyLostPostsActivity, ProfilePageActivity::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recycler_view_my_lost_things)
        recyclerView.layoutManager = LinearLayoutManager(this@MyLostPostsActivity)

        thingsList = arrayListOf()
        thingsIdList = arrayListOf()

        fetchData()

    }

    private fun fetchData() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseFirestore.getInstance()
        db.collection("user").document(userID).collection("Lost Items").get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                querySnapshot.forEach {
                    val docID = it.id
                    thingsIdList.add(docID)
                }
                for (data in querySnapshot.documents) {
//                    db.collection("user").document()
                    val thing: Things? = data.toObject(Things::class.java)
                    if (thing != null) {
                        thingsList.add(thing)
                    }
                }
                recyclerView.adapter = MyLostItemsAdapter(this, thingsList, thingsIdList)
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}