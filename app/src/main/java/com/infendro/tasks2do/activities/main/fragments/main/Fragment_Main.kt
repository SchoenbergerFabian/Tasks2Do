package com.infendro.tasks2do.activities.main.fragments.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.infendro.tasks2do.R
import com.infendro.tasks2do.activities.main.fragments.main.adapters.Adapter
import com.infendro.tasks2do.activities.main.MainActivity
import com.infendro.tasks2do.activities.main.fragments.main.dialogs.Dialog_Create
import kotlinx.android.synthetic.main.fragment_main.*

class Fragment_Main : Fragment() {

    companion object{
        lateinit var adapter : Adapter
        lateinit var recyclerview : RecyclerView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Companion.recyclerview=recyclerview

        //toolbar
        (activity as AppCompatActivity).setSupportActionBar(bottomAppBar)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        updateUI()
    }

    fun updateUI(){
        val list = MainActivity.lists.getCurrentList()

        if(list!=null){

            adapter = Adapter(
                    requireActivity(),
                    list)
            recyclerview.adapter = adapter

            fab_create.show()
            fab_create.setOnClickListener {
                Dialog_Create(requireActivity(),list).show()
            }

        }else{

            recyclerview.adapter=null
            fab_create.hide()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {

            }
            R.id.more -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}