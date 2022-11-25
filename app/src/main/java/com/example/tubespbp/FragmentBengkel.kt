package com.example.tubespbp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespbp.entity.Bengkel
import kotlinx.android.synthetic.main.fragment_bengkel.*
import kotlinx.android.synthetic.main.fragment_bengkel.view.*

class FragmentBengkel : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_bengkel, container, false)

        view.mapActivityBtn.setOnClickListener() {
            val intent= Intent(activity, MapActivity::class.java)
            startActivity(intent)
        }
        // Return the fragment view/layout
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVBengkelAdapter = RVBengkelAdapter(Bengkel.listOfBengkel)

        val rvBengkel : RecyclerView = view.findViewById(R.id.rv_bengkel)

        rvBengkel.layoutManager = layoutManager

        rvBengkel.setHasFixedSize(true)

        rvBengkel.adapter = adapter
    }
}