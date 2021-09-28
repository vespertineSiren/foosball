package com.example.foosball.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T>(
    protected val masterList: MutableList<T> = mutableListOf(),
    val onItemClicked: ((T) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = masterList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder<T>).onBind(masterList[position], onItemClicked)
    }

    abstract class BaseViewHolder<E>(val view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(data: E, listener: ((E) -> Unit)?)
    }
}
