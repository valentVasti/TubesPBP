package com.example.tubespbp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespbp.entity.Bengkel

class FragmentBengkel : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bengkel,container,false)
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