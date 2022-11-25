package com.example.tubespbp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.master.permissionhelper.PermissionHelper
import kotlinx.android.synthetic.main.fragment_bengkel.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class FragmentSearch : Fragment(R.layout.fragment_search) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_search, container, false)

        view.cameraBtn.setOnClickListener() {

            var permissionHelper = PermissionHelper(this, arrayOf(Manifest.permission.CAMERA), 100)
            permissionHelper.denied {
                if (it) {
                    permissionHelper?.openAppDetailsActivity()
                } else {
                    Log.d("Message:", "Permission denied")
                }
            }

            //Request all permission
            permissionHelper.requestAll {
                Log.d("Message:", "All permission granted")
                val intent= Intent(activity, CameraActivity::class.java)
                startActivity(intent)
            }

            //Request individual permission
            permissionHelper.requestIndividual {
                Log.d("Message:", "Individual Permission Granted")
                val intent= Intent(activity, CameraActivity::class.java)
                startActivity(intent)
            }
        }

        return view
    }
}