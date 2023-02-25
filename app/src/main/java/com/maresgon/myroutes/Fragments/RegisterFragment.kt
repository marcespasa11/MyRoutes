package com.maresgon.myroutes.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maresgon.myroutes.R
import com.maresgon.myroutes.databinding.FragmentRegisterBinding
import com.maresgon.myroutes.Classes.User


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {
    lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confpassword: EditText
    lateinit var name: EditText
    lateinit var date: EditText
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*_binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root*/

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_register, container, false)

        // View Bindings
        val context = activity as AppCompatActivity
        email = v.findViewById(R.id.prof_email)
        password = v.findViewById(R.id.prof_password)
        confpassword = v.findViewById(R.id.reg_confpassword)
        name = v.findViewById(R.id.prof_name)
        date = v.findViewById(R.id.prof_date)
        val regbutton: Button = v.findViewById(R.id.button_register)
        //val logbutton: TextView = v.findViewById(R.id.return_button)

        // Initialising auth object
        auth = FirebaseAuth.getInstance()

        regbutton.setOnClickListener {
            signUpUser()
        }

        // switching from signUp Activity to Login Activity
        /*logbutton.setOnClickListener {
            val fragmentManager = context.supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.main_activity, LoginFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }*/



        return v

    }
    @SuppressLint("RestrictedApi")
    private fun signUpUser() {
        val emailText = email.text.toString()
        val passwordText = password.text.toString()
        val confpasswordText = confpassword.text.toString()
        val nameText = name.text.toString()
        val dateText = date.text.toString()

        // check pass
        if (emailText.isBlank() || passwordText.isBlank() || confpasswordText.isBlank() || nameText.isBlank() || dateText.isBlank()) {
            Toast.makeText(activity, "Data can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (passwordText != confpasswordText) {
            Toast.makeText(activity, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        auth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Registered successfully!", Toast.LENGTH_SHORT).show()
                val user = com.maresgon.myroutes.Fragments.auth.currentUser

                //Save user data in firebase
                val userBD = User(emailText, passwordText, nameText, dateText)
                val rootRef = FirebaseFirestore.getInstance()
                val usersRef = rootRef.collection("users")
                usersRef.document(emailText).set(userBD)

                findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
                //Change fragment to login
                /*val context = activity as AppCompatActivity
                val fragmentManager = context.supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.main_activity,LoginFragment())
                transaction.addToBackStack(null)
                transaction.commit()*/
            } else {
                if (passwordText.length < 6){
                    Toast.makeText(
                        activity,
                        "Password too short!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        activity,
                        "Your email is already registered!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.returnButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}
