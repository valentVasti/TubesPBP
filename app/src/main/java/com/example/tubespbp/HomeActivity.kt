package com.example.tubespbp

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import loginRegis.LoginActivity


class HomeActivity : AppCompatActivity() {
    lateinit var botNav : BottomNavigationView
    lateinit var bundle : Bundle
    lateinit var layoutLoading: LinearLayout
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val usernameLogin = getBundle()
        val firstFragment=FragmentBengkel(usernameLogin)
        val secondFragment=FragmentSearch(usernameLogin)
        val thirdFragment=FragmentProfile(usernameLogin)

        layoutLoading = findViewById(R.id.layoutLoading)

        setCurrentFragment(firstFragment)
        botNav = findViewById(R.id.bottom_navigation_view) as BottomNavigationView
        botNav.setOnNavigationItemSelectedListener {
            if(it.itemId ==R.id.bengkelFragment) {
//                setLoading(true)
                setCurrentFragment(firstFragment)
//                setLoading(false)
            }else if(it.itemId == R.id.searchFragment){
                setCurrentFragment(secondFragment)
            }else if(it.itemId == R.id.profileFragment) {
                setCurrentFragment(thirdFragment)
            }else {
                val moveLogin = Intent(this, LoginActivity::class.java)
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

    fun getBundle():Int{
        bundle = intent.getBundleExtra("userData")!!

        var idLogin : Int = bundle.getInt("id")!!

        return idLogin

    }

}