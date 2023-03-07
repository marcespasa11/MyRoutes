package com.maresgon.myroutes.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
/*import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query*/
import com.maresgon.myroutes.Classes.Post
import com.maresgon.myroutes.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */class HomeFragment : Fragment() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("posts")
    private var recyclerAdapter: RecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_home, container, false)

        val button_filters: AppCompatImageButton = v.findViewById(R.id.button_filters)

        button_filters.setOnClickListener {
            val filtered_query : Query

            val filter_diff = filter_difficulty.selectedItem.toString()
            val filter_act = filter_kindOfAct.selectedItem.toString()

            filtered_query = if (filter_diff == "---" && filter_act == "---") {
                db.collection("posts")
            }else if (filter_diff == "---") {
                db.collection("posts")
                    .whereEqualTo("kindOfActivity", filter_act)
            } else if (filter_act == "---") {
                db.collection("posts")
                    .whereEqualTo("difficulty", filter_diff)
            } else{
                db.collection("posts")
                    .whereEqualTo("difficulty", filter_diff)
                    .whereEqualTo("kindOfActivity", filter_act)
            }


            // Actualiza las opciones del adaptador con la nueva consulta
            val options = FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(filtered_query, Post::class.java)
                .build()

            if (recyclerAdapter != null && recyclerAdapter!!.itemCount > 0) {
                recyclerAdapter!!.snapshots.clear()//snapshot.documents.clear()
                recyclerAdapter!!.notifyDataSetChanged()
            }
            recyclerAdapter!!.updateOptions(options)
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Post> =
            FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(collectionReference, Post::class.java)
                .build()

        recyclerAdapter = RecyclerAdapter(firestoreRecyclerOptions)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = recyclerAdapter
    }

    override fun onStart() {
        super.onStart()
        recyclerAdapter?.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerAdapter?.stopListening()
    }
}
