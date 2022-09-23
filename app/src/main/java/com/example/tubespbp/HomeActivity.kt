package com.example.tubespbp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    lateinit var botNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val firstFragment=FragmentBengkel()
        val secondFragment=FragmentSearch()
        val thirdFragment=FragmentProfile()

        setCurrentFragment(firstFragment)
        botNav = findViewById(R.id.bottom_navigation_view) as BottomNavigationView
        botNav.setOnNavigationItemSelectedListener {
            if(it.itemId ==R.id.bengkelFragment) {
                setCurrentFragment(firstFragment)
            }else if(it.itemId == R.id.searchFragment){
                setCurrentFragment(secondFragment)
            }else if(it.itemId == R.id.profileFragment) {
                setCurrentFragment(thirdFragment)
            }else {
                val moveLogin = Intent(this, MainActivity::class.java)
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity)
                builder.setMessage("Are you sure want to logout ?")
                    .setPositiveButton("YES",object: DialogInterface.OnClickListener    {
                        override fun onClick(dialogInterface: DialogInterface,i: Int){
                            finishAndRemoveTask()
                            startActivity(moveLogin)
                        }
                    })
                    .show()
            }
            true
        }

    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.navFragment,fragment)
            commit()
        }
}