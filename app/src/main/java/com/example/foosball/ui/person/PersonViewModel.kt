package com.example.foosball.ui.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foosball.db.FoosBallDatabase
import com.example.foosball.models.Person
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PersonViewModel @Inject constructor(
    private val database: FoosBallDatabase
) :
    ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    // Variable
    private var matchPersonList = listOf<Person>()
    private var matchSpinnerPrimary: Person? = null

    //  MutableLiveData
    private val spinnerPrimaryList by lazy { MutableLiveData<List<String>>() }
    private val personFromDB by lazy { MutableLiveData<Person>() }

    //  LiveData
    fun getSpinnerPrimaryList(): LiveData<List<String>> = spinnerPrimaryList
    fun getPersonFromDB(): LiveData<Person> = personFromDB

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
                        spinnerPrimaryList.postValue(namesList)
                        matchPersonList = list
                    },
                    {}
                )
        )
    }

    fun searchPersonByID(personID: Int) {
        addDisposable(
            database
                .personDao()
                .getPersonById(personID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { personFromDB.postValue(it) },
                    { }
                )
        )
    }

    fun checkPrimaryMatch(spinnerString: String) {
        matchSpinnerPrimary = if (spinnerString.isNotEmpty()) {
            matchPersonList.find { it.nameIDString == spinnerString }
        } else {
            null
        }

        matchSpinnerPrimary?.id?.let { searchPersonByID(it) }
    }

    private val _text = MutableLiveData<String>().apply {
        value = " "
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
