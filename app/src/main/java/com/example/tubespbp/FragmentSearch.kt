package com.example.tubespbp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
            val intent= Intent(activity, CameraActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}