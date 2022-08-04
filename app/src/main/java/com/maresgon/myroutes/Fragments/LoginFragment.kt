package com.maresgon.myroutes.Fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.maresgon.myroutes.Activities.LoggedActivity
import com.maresgon.myroutes.R
import com.maresgon.myroutes.databinding.FragmentLoginBinding

//import com.maresgon.myroutes.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

val auth: FirebaseAuth = FirebaseAuth.getInstance()

class LoginFragment : Fragment() {
    //private var _binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_login, container, false)

        // Get the activity and widget
        val context = activity as AppCompatActivity
        val logButton: TextView = v.findViewById(R.id.button_login)
        val regButton: TextView = v.findViewById(R.id.button_regFragment)
        val email: EditText = v.findViewById(R.id.email_field)
        val password: EditText = v.findViewById(R.id.password_field)

        // Replace activity
        logButton.setOnClickListener {
            val txt_email: String = email.text.toString()
            val txt_password: String = password.text.toString()

            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                Toast.makeText(activity, "Empty Credentials!", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Logged-in successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(activity, LoggedActivity::class.java))
                    } else {
                        Toast.makeText(activity,
                            "Your email or password doesn't exist!",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        // Replace fragment
        regButton.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
            /*val fragmentManager = context.supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.main_activity,RegisterFragment())
            transaction.addToBackStack(null)
            transaction.commit()*/
        }

        return v
    }




    //private var _binding: FragmentLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    /*private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttton_regFragment.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}