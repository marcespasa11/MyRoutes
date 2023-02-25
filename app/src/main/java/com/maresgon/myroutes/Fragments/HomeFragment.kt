package com.maresgon.myroutes.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
/*import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query*/
import com.maresgon.myroutes.Classes.Post
import com.maresgon.myroutes.R
import com.maresgon.myroutes.RecyclerAdapter
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

        val button_filters: TextView = v.findViewById(R.id.button_filters)

        button_filters.setOnClickListener {
            var filtered_query : Query //= FirebaseFirestore.getInstance().collection("posts").whereEqualTo("difficulty", "Medium")

            var filter_diff = filter_difficulty.selectedItem.toString()
            var filter_act = filter_kindOfAct.selectedItem.toString()
            filtered_query = db.collection("posts")
                .whereEqualTo("difficulty", filter_diff)
                .whereEqualTo("kindOfActivity", filter_act)


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
