package com.infendro.tasks2do.ui.main.main.dialogs.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infendro.tasks2do.R
import com.infendro.tasks2do.ui.main.MainActivity.Companion.lists
import com.infendro.tasks2do.ui.main.list.FragmentRenameList
import com.infendro.tasks2do.ui.main.main.FragmentMain
import com.infendro.tasks2do.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.bottomsheetdialog_more.*


class BottomSheetDialogMore : BottomSheetDialogFragment() {

    companion object {
        private val RQ_PREFERENCES = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheetdialog_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewRename.setOnClickListener {
            val currentList = lists.getCurrentList()
            if(currentList!=null){
                FragmentRenameList.list = currentList
                requireActivity().findNavController(R.id.nav).navigate(R.id.action_fragment_Main_to_fragmentRenameList)
            }else{
                //TODO feedback
            }
            dismiss()
        }

        textViewDelete.setOnClickListener {
            val currentList = lists.currentList
            if(currentList!=-1){
                lists.removeList(lists.currentList)
                FragmentMain.updateUI()
            }else{
                //TODO feedback
            }
            dismiss()
        }

        textViewSettings.setOnClickListener {
            dismiss()
            startActivityForResult(Intent(requireActivity(), SettingsActivity::class.java), RQ_PREFERENCES)
        }
    }

}