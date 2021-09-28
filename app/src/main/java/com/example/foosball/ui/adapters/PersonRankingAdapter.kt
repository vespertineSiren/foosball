package com.example.foosball.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foosball.R
import com.example.foosball.models.Person

class PersonRankingAdapter(
    personList: MutableList<Person> = mutableListOf(),
    onItemClicked: ((Person) -> Unit)? = null
) : BaseRecyclerAdapter<Person>(personList, onItemClicked) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_person_ranking, parent, false)
        )

    class ViewHolder(view: View) : BaseViewHolder<Person>(view) {

        private val textPersonRanking: AppCompatTextView =
            view.findViewById(R.id.rowPersonText)

        override fun onBind(data: Person, listener: ((Person) -> Unit)?) {
            textPersonRanking.text = "${adapterPosition + 1}. ${data.nameIDString} "
            textPersonRanking.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                if (adapterPosition == 0) {
                    AppCompatResources.getDrawable(view.context, R.drawable.ic_person_winner_24dp)
                } else {
                    null
                },
                null
            )
            view.rootView.setOnClickListener {
                listener?.invoke(data)
            }
        }
    }

    //  TODO: Implement Diffutil in future, instead of expensive notifyDataSetChanged()
    fun setPersons(data: List<Person>) {
        if (masterList.size > 0) masterList.clear()
        masterList.addAll(data)
        notifyDataSetChanged()
    }
}
