package com.example.foosball.ui.person

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.foosball.databinding.FragmentPersonBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PersonFragment @Inject constructor() : DaggerFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private val personViewModel by viewModels<PersonViewModel> { vmFactory }
    private var _binding: FragmentPersonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPersonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupObservers()

        return root
    }

    private fun setupObservers() {
        personViewModel.apply {
            searchPersonFlowable()

            getSpinnerPrimaryList().observe(
                viewLifecycleOwner,
                { primaryList ->
                    val newAdapter =
                        ArrayAdapter(requireContext(), R.layout.simple_spinner_item, primaryList)
                            .apply {
                                setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                            }
                    binding.personSelectSpinner.adapter = newAdapter
                    binding.personSelectSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                position: Int,
                                p3: Long
                            ) {
                                personViewModel.checkPrimaryMatch(primaryList[position])
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                // Do Nothing
                            }
                        }
                }
            )

            getPersonFromDB().observe(
                viewLifecycleOwner,
                {
                    binding.personWinTotal.setText(it.winTotal.toString())
                    binding.personLoseTotal.setText(it.loseTotal.toString())
                    binding.personTiesTotal.setText(it.tieTotal.toString())
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
