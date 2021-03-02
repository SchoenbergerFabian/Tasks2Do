package com.infendro.tasks2do.activities.main.fragments.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.infendro.tasks2do.List
import com.infendro.tasks2do.R
import com.infendro.tasks2do.activities.main.fragments.main.adapters.Adapter_CheckedTasks
import com.infendro.tasks2do.activities.main.fragments.main.adapters.Adapter_UncheckedTasks
import com.infendro.tasks2do.activities.main.MainActivity
import com.infendro.tasks2do.activities.main.fragments.main.dialogs.Dialog_Create
import kotlinx.android.synthetic.main.fragment_main.*

class Fragment_Main : Fragment() {

    companion object{
        lateinit var scrollview : ScrollView

        lateinit var adapter_unchecked : Adapter_UncheckedTasks
        lateinit var adapter_checked : Adapter_CheckedTasks

        private lateinit var dropdown_showChecked : LinearLayout
        private lateinit var recyclerview_checkedTasks : RecyclerView
        fun showEmpty(list: List?){
            if(list==null||list.checkedTasks.isEmpty()){
                dropdown_showChecked.visibility = View.GONE
                dropdown_showChecked.findViewById<ImageView>(
                    R.id.imageview_ic_dropdown
                ).rotation=0f
                recyclerview_checkedTasks.visibility=View.GONE
                recyclerview_checkedTasks.alpha=0f
            }else{
                dropdown_showChecked.visibility = View.VISIBLE
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

        Companion.scrollview = scrollview

        Companion.dropdown_showChecked = dropdown_showChecked
        Companion.recyclerview_checkedTasks = recyclerview_checkedTasks

        dropdown_showChecked.setOnClickListener {
            if(recyclerview_checkedTasks.visibility==View.GONE){
                recyclerview_checkedTasks.visibility = View.VISIBLE
                recyclerview_checkedTasks.animate().setDuration(100).alpha(1f).setListener(null)
                imageview_ic_dropdown.animate().rotation(180f)
                scrollview.post {
                    scrollview.fullScroll(View.FOCUS_DOWN)
                }
            }else{
                recyclerview_checkedTasks.animate().setDuration(100).alpha(0f).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        recyclerview_checkedTasks.visibility = View.GONE
                    }
                })
                imageview_ic_dropdown.animate().rotation(0f)
            }
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        updateUI()
    }

    fun updateUI(){
        val list = MainActivity.lists.getCurrentList()

        if(list!=null){

            textview_title.text = list.title

            adapter_unchecked =
                Adapter_UncheckedTasks(
                    requireActivity(),
                    list
                )
            recyclerview_tasks.adapter =
                adapter_unchecked

            adapter_checked =
                Adapter_CheckedTasks(
                    requireActivity(),
                    list
                )
            recyclerview_checkedTasks.adapter =
                adapter_checked

            button_create.visibility=View.VISIBLE
            button_create.setOnClickListener {
                Dialog_Create(requireActivity(),list).show()
            }

        }else{

            textview_title.text=""
            recyclerview_tasks.adapter=null
            recyclerview_checkedTasks.adapter=null
            button_create.visibility=View.GONE

        }

        showEmpty(
            list
        )
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