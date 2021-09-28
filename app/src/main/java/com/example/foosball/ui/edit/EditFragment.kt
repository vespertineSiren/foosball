package com.example.foosball.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.foosball.databinding.FragmentEditBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EditFragment @Inject constructor() : DaggerFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private val editViewModel by viewModels<EditViewModel> { vmFactory }
    private var _binding: FragmentEditBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        editViewModel.text.observe(
            viewLifecycleOwner,
            {
                textView.text = it
            }
        )
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
