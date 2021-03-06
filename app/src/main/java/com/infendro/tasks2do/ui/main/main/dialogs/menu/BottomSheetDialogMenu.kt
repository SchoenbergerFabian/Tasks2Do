package com.infendro.tasks2do.ui.main.main.dialogs.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infendro.tasks2do.Connection.Account
import com.infendro.tasks2do.R
import com.infendro.tasks2do.ui.main.MainActivity.Companion.lists
import com.infendro.tasks2do.ui.main.list.FragmentCreateList
import com.infendro.tasks2do.ui.main.main.ViewModelMain
import kotlinx.android.synthetic.main.bottomsheetdialog_menu.*


class BottomSheetDialogMenu : BottomSheetDialogFragment() {

    companion object{
        private lateinit var bottomSheetDialogMenu: BottomSheetDialogMenu

        fun dismiss(){
            bottomSheetDialogMenu.dismiss()
        }
    }

    private lateinit var adapterLists: AdapterLists

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheetdialog_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(Account.username!=""){
            textViewUsername.text = Account.username
            textViewUsername.visibility = View.VISIBLE
            textViewNoUsername.visibility = View.GONE
        }else{
            textViewUsername.visibility = View.GONE
            textViewNoUsername.visibility = View.VISIBLE
        }

        layoutUsername.setOnClickListener {
            toLoginInfo()
        }

        bottomSheetDialogMenu=this

        textViewCreateList.setOnClickListener {
            FragmentCreateList.lists = lists
            requireActivity().findNavController(R.id.nav).navigate(R.id.action_fragment_Main_to_fragmentCreateList)
            dismiss()
        }

        val model : ViewModelMain by activityViewModels()
        adapterLists = AdapterLists(requireActivity(), lists, model)
        recyclerViewLists.adapter = adapterLists
    }

    private fun toLoginInfo(){
        requireActivity().findNavController(R.id.nav).navigate(R.id.action_fragment_Main_to_fragmentUser)
        dismiss()
    }

}