package com.infendro.tasks2do.ui.main.main.dialogs.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infendro.tasks2do.List
import com.infendro.tasks2do.R
import com.infendro.tasks2do.ui.main.MainActivity
import com.infendro.tasks2do.ui.main.MainActivity.Companion.lists
import com.infendro.tasks2do.ui.main.main.FragmentMain
import kotlinx.android.synthetic.main.bottomsheetdialog_lists.*


class BottomSheetDialogLists : BottomSheetDialogFragment() {

    companion object{
        private lateinit var bottomSheetDialogLists: BottomSheetDialogLists

        fun dismiss(){
            bottomSheetDialogLists.dismiss()
        }
    }

    private lateinit var adapterLists: AdapterLists

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheetdialog_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetDialogLists=this

        textViewCreateList.setOnClickListener {
            val list = List()
            list.title = "New list"
            lists.addList(list)

            FragmentMain.updateUI()
            MainActivity.save(requireActivity())

            dismiss()
        }

        adapterLists = AdapterLists(requireActivity(), lists)
        recyclerViewLists.adapter = adapterLists
    }

}