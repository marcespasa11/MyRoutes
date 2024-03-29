package com.maresgon.myroutes.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maresgon.myroutes.Classes.Post
import com.maresgon.myroutes.Fragments.HomeFragment
import com.maresgon.myroutes.Fragments.MapFragment
import com.maresgon.myroutes.Fragments.ProfileFragment

import com.maresgon.myroutes.R
import kotlinx.android.synthetic.main.activity_logged.*


class SharedViewModel: ViewModel() {
    val selectedPlace = MutableLiveData<Post>()
}

class LoggedActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)

        val firstFragment = HomeFragment()
        val secondFragment = MapFragment()
        //val thirdFragment = MapFragment()
        val thirdFragment= ProfileFragment()

        setCurrentFragment(firstFragment)

        val intent = intent
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        val str = intent.getStringExtra("edad")
        println(str)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(firstFragment)
                R.id.new_post -> setCurrentFragment(secondFragment)
                R.id.profile -> setCurrentFragment(thirdFragment) //Fer profileFragment

            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}
