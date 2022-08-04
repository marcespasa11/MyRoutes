package com.maresgon.myroutes.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
/*import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query*/
import com.maresgon.myroutes.Classes.Post
import com.maresgon.myroutes.R
import com.maresgon.myroutes.RecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.fragment_logged.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoggedFragment : Fragment() {

    //private var _binding: FragmentFirstBinding? = null
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference : CollectionReference = db.collection("posts")
    var recyclerAdapter: RecyclerAdapter ?= null
    //private var layoutManager: RecyclerView.LayoutManager? = null
    //private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val v: View = inflater.inflate(R.layout.fragment_logged, container, false)



        return v //binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        //RecyclerView en local
        /*val routesRecyclerView = ViewBindings.findChildViewById<RecyclerView>(view, R.id.recyclerView)
        routesRecyclerView.apply {

            layoutManager = LinearLayoutManager(requireContext())
            routesRecyclerView?.layoutManager
            adapter = RecyclerAdapter()
            routesRecyclerView?.adapter = adapter
        }*/

    }

    private fun setUpRecyclerView(){
        val query : Query = collectionReference
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Post> = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .build()

        recyclerAdapter = RecyclerAdapter(firestoreRecyclerOptions)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = recyclerAdapter
    }

    override fun onStart() {
        super.onStart()
        recyclerAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerAdapter!!.stopListening()
    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}