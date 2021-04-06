package com.infendro.tasks2do.ui.main.main

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.infendro.tasks2do.R
import com.infendro.tasks2do.List
import com.infendro.tasks2do.ui.main.MainActivity
import com.infendro.tasks2do.ui.main.main.dialogs.DialogCreate
import com.infendro.tasks2do.ui.main.main.dialogs.menu.BottomSheetDialogMenu
import com.infendro.tasks2do.ui.main.main.dialogs.more.BottomSheetDialogMore
import kotlinx.android.synthetic.main.fragment_main.*

class FragmentMain : Fragment() {

    companion object{
        lateinit var activity: Activity

        lateinit var adapter : AdapterList
        lateinit var recyclerView : RecyclerView
        lateinit var fabCreate : FloatingActionButton

        fun updateUI(list: List?){
            if(list!=null){

                adapter = AdapterList(
                    activity,
                    list)
                recyclerView.adapter = adapter

                fabCreate.show()
                fabCreate.setOnClickListener {
                    DialogCreate(
                        activity,
                        list
                    ).show()
                }

            }else{

                recyclerView.adapter=null
                fabCreate.hide()

            }
        }
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

        requireActivity().currentFocus?.clearFocus()

        //toolbar
        (activity as AppCompatActivity).setSupportActionBar(bottomAppBar)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        //UI
        Companion.activity = requireActivity()
        Companion.recyclerView=recyclerView
        Companion.fabCreate=fabCreate

        val model : ViewModelMain by activityViewModels()

        model.loadCurrentList()
        model.list.observe(viewLifecycleOwner, { list ->
            updateUI(list)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                BottomSheetDialogMenu()
                    .show(requireActivity().supportFragmentManager, null)
            }
            R.id.more -> {
                BottomSheetDialogMore()
                    .show(requireActivity().supportFragmentManager, null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}