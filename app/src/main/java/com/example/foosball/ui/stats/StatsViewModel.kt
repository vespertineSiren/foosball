package com.example.foosball.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foosball.db.FoosBallDatabase
import com.example.foosball.models.Match
import com.example.foosball.models.Person
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StatsViewModel @Inject constructor(
    private val database: FoosBallDatabase
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    // Variable
    private var refMatchList = listOf<Match>()
    private var matchPersonList = mutableListOf<Person>()
    private var matchSpinnerPrimary: Person? = null
    private var matchPrimaryScore: Int = 0
    private var matchSpinnerSecondary: Person? = null
    private var matchSecondaryScore: Int = 0
    private var editingMatch: Match? = null
    private var isNewMatch: Boolean = true
    private var isResettingMatch: Boolean = true

    //  MutableLiveData
    private val personWinRankings by lazy { MutableLiveData<List<Person>>() }
    private val personRankings by lazy { MutableLiveData<List<Person>>() }
    private val matchRankings by lazy { MutableLiveData<List<Match>>() }
    private val spinnerPrimaryList by lazy { MutableLiveData<List<String>>() }
    private val spinnerSecondaryList by lazy { MutableLiveData<List<String>>() }
    private val matchSaveButtonEnabled by lazy { MutableLiveData<Boolean>() }
    private val matchEditingCurrent by lazy { MutableLiveData<Match>() }
    private val matchResetting by lazy { MutableLiveData<Boolean>() }

    //  LiveData
    fun getPersonWinRankings(): LiveData<List<Person>> = personWinRankings
    fun getPersonRankings(): LiveData<List<Person>> = personRankings
    fun getMatchRankings(): LiveData<List<Match>> = matchRankings
    fun getSpinnerPrimaryList(): LiveData<List<String>> = spinnerPrimaryList
    fun getSpinnerSecondaryList(): LiveData<List<String>> = spinnerSecondaryList
    fun getMatchSaveButtonEnabled(): LiveData<Boolean> = matchSaveButtonEnabled
    fun getMatchEditingCurrent(): LiveData<Match> = matchEditingCurrent
    fun getMatchResetting(): LiveData<Boolean> = matchResetting

    fun searchMatchRankingsFlowable() {
        addDisposable(
            database.matchDao()
                .getAllMatchesFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { matchList ->
                        if (matchList.isNotEmpty()) {
                            refMatchList = matchList
                            matchRankings.postValue(matchList)
                        } else {
                            createInitialData()
                        }
                    },
                    {
                        // TODO: Stop loading view, Error handling
                    }
                )
        )
    }

    fun searchPersonFlowable() {
        addDisposable(
            database.personDao()
                .getAllPersonsFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        val namesList = list.map { it.nameIDString }.toMutableList().apply {
                            add(0, " ")
                        }
                        matchPersonList = list.toMutableList()
                        spinnerPrimaryList.postValue(namesList)
                        spinnerSecondaryList.postValue(namesList)

                        val sortedWins = list.sortedByDescending { it.winTotal }
                        personWinRankings.postValue(sortedWins)

                        val sortedMatchFrequency = list.sortedByDescending { it.matchTotal }
                        personRankings.postValue(sortedMatchFrequency)
                    },
                    {}
                )
        )
    }

    private fun createInitialData() {
        val matchInitList = Match.createInitialList()
        val personList = Person.createDistinctPersonFromMatches(matchInitList)
        Match.createRoomReadyList(matchInitList, personList)
        saveStatsMatchList(matchInitList)
        saveStatsPersonList(personList)
    }

    //  Only used when saving a completely new match
    private fun saveStatsMatch(match: Match) {
        addDisposable(
            database
                .matchDao()
                .insert(match)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        handleResetButtonClick()
                    },
                    { handleResetButtonClick() }
                )
        )
    }

    private fun saveStatsMatchList(matchesList: List<Match>) {
        addDisposable(
            database
                .matchDao()
                .insertAllMatches(matchesList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { },
                    { }
                )
        )
    }

    private fun saveStatsPersonList(personList: List<Person>) {
        addDisposable(
            database
                .personDao()
                .insertAllPersons(personList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { }, { }
                )
        )
    }

    private fun updateMatch(match: Match) {
        addDisposable(
            database
                .matchDao()
                .update(match)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { handleResetButtonClick() },
                    { handleResetButtonClick() }
                )
        )
    }

    fun updatePersonsList(list: List<Person>) {
        addDisposable(
            database
                .personDao()
                .updateAll(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { },
                    { }
                )
        )
    }

    fun handlePrimaryPointChange(point: Int) {
        matchPrimaryScore = point
        verifySpinnerMatch()
    }

    fun handleSecondaryPointChange(point: Int) {
        matchSecondaryScore = point
        verifySpinnerMatch()
    }

    fun handleMatchHistoryClick(match: Match) {
        editingMatch = match
        isNewMatch = false
        matchSpinnerPrimary = match.personPrimary
        matchPrimaryScore = match.personPrimaryScore
        matchSpinnerSecondary = match.personSecondary
        matchSecondaryScore = match.personSecondaryScore
        spinnerPrimaryList.postValue(listOf(match.personPrimary.nameIDString))
        spinnerSecondaryList.postValue(listOf(match.personSecondary.nameIDString))
        matchEditingCurrent.postValue(match)
    }

    fun handleSaveButtonClick() {
        if (!isNewMatch) {
            editingMatch?.let {
                it.apply {
                    personPrimaryScore = matchPrimaryScore
                    personSecondaryScore = matchSecondaryScore
                }
                updateMatch(it)
            }
            isNewMatch = true
        } else {
            matchSpinnerPrimary?.let { safePrimary ->
                matchSpinnerSecondary?.let { safeSecondary ->
                    val newMatch = Match(
                        safePrimary,
                        matchPrimaryScore,
                        safeSecondary,
                        matchSecondaryScore
                    )
                    newMatch.validatePersonsNewMatch(safePrimary, safeSecondary)
                    saveStatsMatch(newMatch)
                    updatePersonsList(listOf(safePrimary, safeSecondary))
                }
            }
        }
    }

    fun handleResetButtonClick() {
        val namesList = matchPersonList.map { it.nameIDString }.toMutableList().apply {
            add(0, " ")
        }

        matchSpinnerPrimary = null
        spinnerPrimaryList.postValue(namesList)
        matchSpinnerSecondary = null
        spinnerSecondaryList.postValue(namesList)
        matchResetting.postValue(true)

        editingMatch = null
        isNewMatch = true
    }

    fun checkPrimaryMatch(spinnerString: String) {
        matchSpinnerPrimary = if (spinnerString.isNotEmpty()) {
            matchPersonList.find { it.nameIDString == spinnerString }
        } else {
            null
        }
        verifySpinnerMatch()
    }

    fun checkSecondaryMatch(spinnerString: String) {
        matchSpinnerSecondary = if (spinnerString.isNotEmpty()) {
            matchPersonList.find { it.nameIDString == spinnerString }
        } else {
            null
        }
        verifySpinnerMatch()
    }

    private fun verifySpinnerMatch() {
        val isEnabled = when {
            ((matchSpinnerPrimary == null) || (matchSpinnerSecondary == null)) -> false
            (matchSpinnerPrimary?.userID != matchSpinnerSecondary?.userID) -> true
            else -> false
        }
        matchSaveButtonEnabled.postValue(isEnabled)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "Match"
    }
    val text: LiveData<String> = _text

    private fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
