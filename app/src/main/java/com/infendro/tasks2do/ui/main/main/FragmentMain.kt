package com.infendro.tasks2do.ui.main.main

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.infendro.tasks2do.R
import com.infendro.tasks2do.ui.main.MainActivity
import com.infendro.tasks2do.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_main.*

class FragmentMain : Fragment() {

    companion object{
        private val RQ_PREFERENCES = 1

        lateinit var adapter : AdapterList
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

        recyclerview =recyclerView

        //toolbar
        (activity as AppCompatActivity).setSupportActionBar(bottomAppBar)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        updateUI()
    }

    fun updateUI(){
        val list = MainActivity.lists.getCurrentList()

        if(list!=null){

            adapter = AdapterList(
                    requireActivity(),
                    list)
            recyclerView.adapter = adapter

            fabCreate.show()
            fabCreate.setOnClickListener {
                DialogCreate(requireActivity(),list).show()
            }

        }else{

            recyclerView.adapter=null
            fabCreate.hide()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                //TODO
            }
            R.id.more -> {
                //TODO temp
                startActivityForResult(Intent(requireActivity(), SettingsActivity::class.java), RQ_PREFERENCES)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}