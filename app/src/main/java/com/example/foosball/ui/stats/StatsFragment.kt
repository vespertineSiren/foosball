package com.example.foosball.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.foosball.databinding.FragmentStatsBinding
import com.example.foosball.ui.adapters.MatchRankingsAdapter
import com.example.foosball.ui.adapters.PersonRankingAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class StatsFragment @Inject constructor() : DaggerFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private val statsViewModel by viewModels<StatsViewModel> { vmFactory }
    private var _binding: FragmentStatsBinding? = null

    private lateinit var matchAdapter: MatchRankingsAdapter
    private lateinit var personAdapter: PersonRankingAdapter
    private lateinit var personWinAdapter: PersonRankingAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViewListeners()
        setupAdapters()
        setupObservers()

        val textView: TextView = binding.textHome
        statsViewModel.text.observe(
            viewLifecycleOwner,
            {
                textView.text = it
            }
        )
        return root
    }

    private fun setupViewListeners() {
        binding.apply {
            statsEditPointsPrimary.doAfterTextChanged {
                if (!it.toString()
                    .isNullOrEmpty()
                ) statsViewModel.handlePrimaryPointChange(it.toString().toInt())
            }

            statsEditPointsSecondary.doAfterTextChanged {
                if (!it.toString()
                    .isNullOrEmpty()
                ) statsViewModel.handleSecondaryPointChange(it.toString().toInt())
            }

            statsButtonSave.setOnClickListener {
                statsViewModel.handleSaveButtonClick()
            }

            statsButtonReset.setOnClickListener {
                statsViewModel.handleResetButtonClick()
            }
        }
    }

    private fun setupObservers() {
        statsViewModel.apply {
            searchMatchRankingsFlowable()
            searchPersonFlowable()

            getMatchRankings().observe(
                viewLifecycleOwner,
                {
                    matchAdapter.setMatches(it)
                }
            )

            getPersonRankings().observe(
                viewLifecycleOwner,
                {
                    personAdapter.setPersons(it)
                }
            )

            getPersonWinRankings().observe(
                viewLifecycleOwner,
                {
                    personWinAdapter.setPersons(it)
                }
            )

            getSpinnerPrimaryList().observe(
                viewLifecycleOwner,
                { primaryList ->
                    val newAdapter =
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            primaryList
                        )
                            .apply {
                                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            }
                    binding.statsSpinnerPrimary.adapter = newAdapter
                    binding.statsSpinnerPrimary.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                position: Int,
                                p3: Long
                            ) {
                                statsViewModel.checkPrimaryMatch(primaryList[position])
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                // Do Nothing
                            }
                        }
                }
            )

            getSpinnerSecondaryList().observe(
                viewLifecycleOwner,
                { secondaryList ->
                    val newAdapter =
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            secondaryList
                        )
                            .apply {
                                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            }
                    binding.statsSpinnerSecondary.adapter = newAdapter
                    binding.statsSpinnerSecondary.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                position: Int,
                                p3: Long
                            ) {
                                statsViewModel.checkSecondaryMatch(secondaryList[position])
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                // Do Nothing 
                            }
                        }
                }
            )

            getMatchSaveButtonEnabled().observe(
                viewLifecycleOwner,
                {
                    binding.statsButtonSave.isEnabled = it
                }
            )

            getMatchEditingCurrent().observe(
                viewLifecycleOwner,
                {
                    binding.statsEditPointsPrimary.setText(it.personPrimaryScore.toString())
                    binding.statsEditPointsSecondary.setText(it.personSecondaryScore.toString())
                }
            )

            getMatchResetting().observe(
                viewLifecycleOwner,
                { resetPoints ->
                    if (resetPoints) {
                        binding.statsEditPointsPrimary.setText("")
                        binding.statsEditPointsSecondary.setText("")
                    }
                }
            )
        }
    }

    private fun setupAdapters() {
        matchAdapter = MatchRankingsAdapter { match ->
            statsViewModel.handleMatchHistoryClick(match)
        }

        personAdapter = PersonRankingAdapter()
        personWinAdapter = PersonRankingAdapter()

        binding.statsRecyclerPersonWin.adapter = personWinAdapter
        binding.statsRecyclerPerson.adapter = personAdapter
        binding.statsRecyclerMatch.adapter = matchAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
