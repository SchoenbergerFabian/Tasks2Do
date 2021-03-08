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
import com.infendro.tasks2do.ui.DialogDateTimePicker

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
            navigateBack()
        }

        editTextTitle.setText(task?.title)
        editTextTitle.doOnTextChanged { text, _, _, _ ->
            task?.title = text.toString()
        }

        editTextDetails.setText(task?.details)
        editTextDetails.doOnTextChanged{ text, _, _, _ ->
            task?.details = text.toString()
        }

        textViewDue.text = task?.getDueString(requireActivity().getString(R.string.pattern_date),requireActivity().getString(R.string.pattern_time))
        DialogDateTimePicker(requireActivity(),task).show()

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

    fun setCheckedImage(){
        when(task?.checked){
            true -> {
                imageButtonCheck.setImageResource(R.drawable.ic_checked)
            }
            false -> {
                imageButtonCheck.setImageResource(R.drawable.ic_unchecked)
            }
        }
    }

    fun navigateBack(){
        requireActivity().currentFocus?.clearFocus()
    }

}