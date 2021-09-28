package com.example.foosball.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foosball.db.FoosBallDatabase
import javax.inject.Inject

class EditViewModel @Inject constructor(
    private val database: FoosBallDatabase
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}
