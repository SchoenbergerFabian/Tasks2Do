package com.infendro.tasks2do.activities.main.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import kotlinx.android.synthetic.main.fragment_detail.*
import com.infendro.tasks2do.List

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
            findNavController().navigate(R.id.action_fragmentDetail_to_fragment_Main)
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
            findNavController().navigate(R.id.action_fragmentDetail_to_fragment_Main)
        }

        editTextTitle.setText(task?.title)
        //TODO ontextchanged

        editTextDetails.setText(task?.details)
        //TODO ontextchanged

        textViewDue.text = task?.getDueString(requireActivity().getString(R.string.pattern_date),requireActivity().getString(R.string.pattern_time))
        //TODO datetimepicker onclick in layout

        //TODO setup imagebuttoncheck icon
        imageButtonCheck.setOnClickListener {
            when(task?.checked){
                true -> {
                    list?.uncheck(index)
                }
                false -> {
                    list?.check(index)
                }
            }
            findNavController().navigate(R.id.action_fragmentDetail_to_fragment_Main)
        }
    }

}