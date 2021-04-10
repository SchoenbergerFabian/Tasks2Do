package com.infendro.tasks2do.ui.main.main.dialogs.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Storage.Account
import com.infendro.tasks2do.Storage.Connection
import com.infendro.tasks2do.Storage.Storage
import com.infendro.tasks2do.ui.main.MainActivity
import com.infendro.tasks2do.ui.main.MainActivity.Companion.lists
import com.infendro.tasks2do.ui.main.list.FragmentRenameList
import com.infendro.tasks2do.ui.main.main.FragmentMain
import com.infendro.tasks2do.ui.main.main.ViewModelMain
import com.infendro.tasks2do.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.bottomsheetdialog_more.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
            if(lists.currentList!=-1){
                val currentList = lists.getCurrentList()
                lists.removeList(lists.currentList)
                MainActivity.save(requireActivity())
                if(Account.isLoggedIn()){
                    if(Connection.hasInternetConnection(requireActivity())){
                        GlobalScope.launch(Dispatchers.IO) {
                            Storage.removeList(currentList!!)
                        }
                    }else{
                        //TODO
                    }
                }
                val model : ViewModelMain by activityViewModels()
                model.loadCurrentList()
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