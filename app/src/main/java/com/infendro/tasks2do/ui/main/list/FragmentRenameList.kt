package com.infendro.tasks2do.ui.main.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.infendro.tasks2do.List
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Connection.Account
import com.infendro.tasks2do.Connection.Connection
import com.infendro.tasks2do.Connection.Storage
import com.infendro.tasks2do.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_create_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FragmentRenameList : Fragment() {

    companion object {
        lateinit var list: List
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        imageButtonClose.setOnClickListener { navigateBack() }

        editTextTitle.setText(list.title)
        makeValid(list.title)

        editTextTitle.doOnTextChanged { text, _, _, _ ->
            if(text.toString() == ""){
                makeInvalid()
            }else {
                makeValid(text.toString())
            }
        }

        editTextTitle.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun makeValid(title: String){
        buttonSave.setTextColor(requireActivity().getColor(R.color.colorAccent))
        buttonSave.setOnClickListener {
            list.title=title
            MainActivity.save(requireActivity())
            if(Account.isLoggedIn()){
                if(Connection.hasInternetConnection(requireActivity())){
                    GlobalScope.launch(Dispatchers.IO) {
                        Storage.editList(list)
                    }
                }else{
                    //TODO
                }
            }
            navigateBack()
        }
    }

    private fun makeInvalid(){
        buttonSave.setTextColor(requireActivity().getColor(R.color.invalid))
        buttonSave.setOnClickListener(null)
    }

    private fun navigateBack(){
        findNavController().navigate(R.id.action_fragmentRenameList_to_fragment_Main)
    }
}