package com.maresgon.myroutes.Activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.maresgon.myroutes.Fragments.LoggedFragment
import com.maresgon.myroutes.Fragments.PublishFragment
import com.maresgon.myroutes.R
import com.maresgon.myroutes.databinding.ActivityLoggedBinding
import kotlinx.android.synthetic.main.activity_logged.*

class LoggedActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoggedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)

        val firstFragment= LoggedFragment()
        val secondFragment= PublishFragment()
        //val thirdFragment=ProfileFragment()

        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(firstFragment)
                R.id.new_post->setCurrentFragment(secondFragment)
                R.id.profile->setCurrentFragment(secondFragment) //Fer profileFragment

            }
            true
        }
        /*binding = ActivityLoggedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)*/

        //Crec que te a vore amb el navigation (jetpack)
        /*val navController = findNavController(R.id.nav_host_fragment_content_logged)
        appBarConfiguration = AppBarConfiguration(navController.graph)*/
        //setupActionBarWithNavController(navController, appBarConfiguration)

        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            //gastant nav_graph3
            //val navController = findNavController(R.id.fragmentContainerView)

            //gastar nav graph2
            //findNavController(view).navigate(R.id.action_FirstFragment_to_publishFragment)
            //startActivity(Intent(this, PublishActivity::class.java))
            //finish()
        }*/
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_logged)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/
}