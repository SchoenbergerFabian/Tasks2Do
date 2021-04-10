package com.infendro.tasks2do.activities.ui.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import kotlinx.android.synthetic.main.fragment_detail.*
import com.infendro.tasks2do.List
import com.infendro.tasks2do.Storage.Account
import com.infendro.tasks2do.Storage.Connection
import com.infendro.tasks2do.Storage.Storage
import com.infendro.tasks2do.ui.main.DialogDateTimePicker
import com.infendro.tasks2do.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentDetail : Fragment() {
    private var list: List? = null
    private var task: Task? = null
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.get("LIST") as List
            task = it.get("TASK") as Task
            index = it.getInt("INDEX")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonBack.setOnClickListener {
            navigateBack()
        }

        imageButtonDelete.setOnClickListener {
            when(task?.checked){
                true -> {
                    list?.checkedTasks?.removeAt(index)
                }
                false -> {
                    list?.uncheckedTasks?.removeAt(index)
                }
            }
            if(Account.isLoggedIn()){
                if(Connection.hasInternetConnection(requireActivity())){
                    GlobalScope.launch(Dispatchers.IO) {
                        Storage.removeTask(task!!)
                    }
                }else{
                    //TODO
                }
            }
            navigateBack()
        }

        editTextTitle.setText(task?.title)
        editTextTitle.doOnTextChanged { text, _, _, _ ->
            task?.title = text.toString()
            MainActivity.save(requireActivity())
        }

        editTextDetails.setText(task?.details)
        editTextDetails.doOnTextChanged{ text, _, _, _ ->
            task?.details = when(text.toString()) {"" -> null else -> text.toString()}
            MainActivity.save(requireActivity())
        }

        val dueString = task?.getDueString(requireActivity().getString(R.string.pattern_date),requireActivity().getString(R.string.pattern_time))
        if(dueString!=null){
            textViewDue.hint = ""
            textViewDue.text = dueString
            imageButtonRemove.visibility=View.VISIBLE
        }
        due.setOnClickListener {
            val dateTimePicker = DialogDateTimePicker(requireActivity(),task)
            dateTimePicker.setOnDismissListener {
                MainActivity.save(requireActivity())
                val dueString = task?.getDueString(requireActivity().getString(R.string.pattern_date,),requireActivity().getString(R.string.pattern_time,))
                if(dueString!=null){
                    textViewDue.hint = ""
                    textViewDue.text = dueString
                    imageButtonRemove.visibility=View.VISIBLE
                }
            }
            dateTimePicker.show()
        }

        imageButtonRemove.setOnClickListener {
            textViewDue.hint = activity?.getString(R.string.add_datetime)
            textViewDue.text = ""
            imageButtonRemove.visibility=View.GONE
            task?.dueTime=null
            task?.dueDate=null

            MainActivity.save(requireActivity())
        }

        setCheckedImage()
        imageButtonCheck.setOnClickListener {
            when(task?.checked){
                true -> {
                    list?.uncheck(index)
                }
                false -> {
                    list?.check(index)
                }
            }
            setCheckedImage()
            navigateBack()
        }
    }

    private fun setCheckedImage(){
        when(task?.checked){
            true -> {
                imageButtonCheck.setImageResource(R.drawable.ic_checked)
            }
            false -> {
                imageButtonCheck.setImageResource(R.drawable.ic_unchecked)
            }
        }
    }

    private fun navigateBack(){
        MainActivity.save(requireActivity())
        requireActivity().currentFocus?.clearFocus()
        findNavController().navigate(R.id.action_fragmentDetail_to_fragment_Main)
    }

}