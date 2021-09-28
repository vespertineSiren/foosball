package com.example.foosball.ui.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foosball.R
import com.example.foosball.models.Match

class MatchRankingsAdapter(
    matchList: MutableList<Match> = mutableListOf(),
    onItemClicked: ((Match) -> Unit)? = null
) : BaseRecyclerAdapter<Match>(matchList, onItemClicked) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_match_ranking, parent, false)
        )

    internal class ViewHolder(view: View) : BaseViewHolder<Match>(view) {

        private val textPrimaryNameScore: AppCompatTextView =
            view.findViewById(R.id.rowMatchPrimaryNamePoints)

        private val textSecondaryNameScore: AppCompatTextView =
            view.findViewById(R.id.rowMatchSecondaryNamePoints)

        override fun onBind(data: Match, listener: ((Match) -> Unit)?) {

            textPrimaryNameScore.text =
                view.context.getString(
                    R.string.row_match_name_score,
                    data.personPrimary.name,
                    data.personPrimaryScore
                )

            textSecondaryNameScore.text =
                view.context.getString(
                    R.string.row_match_name_score,
                    data.personSecondary.name,
                    data.personSecondaryScore
                )

            val primaryWinnerDrawable: Drawable? = if (data.winner == data.personPrimary) {
                AppCompatResources.getDrawable(view.context, R.drawable.ic_person_winner_24dp)
            } else {
                null
            }
            val secondaryWinnerDrawable: Drawable? = if (data.winner == data.personSecondary) {
                AppCompatResources.getDrawable(view.context, R.drawable.ic_person_winner_24dp)
            } else {
                null
            }

            textPrimaryNameScore.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                primaryWinnerDrawable,
                null
            )
            textSecondaryNameScore.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                secondaryWinnerDrawable,
                null
            )

            view.rootView.setOnClickListener {
                listener?.invoke(data)
            }
        }
    }

    //  TODO: Implement Diffutil in future, instead of expensive notifyDataSetChanged()
    fun setMatches(data: List<Match>) {
        if (masterList.size > 0) masterList.clear()
        masterList.addAll(data)
        notifyDataSetChanged()
    }
}
