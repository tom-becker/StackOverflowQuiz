package com.tombecker.stackoverflowquiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tombecker.stackoverflowquiz.R
import com.tombecker.stackoverflowquiz.model.AnswerModel
import kotlinx.android.synthetic.main.answer_list_item.view.*

class AnswerListAdapter(private val answerList: ArrayList<AnswerModel>,
                        val answerClick: (Int) -> Unit): RecyclerView.Adapter<AnswerListAdapter.AnswerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.answer_list_item, parent, false)
        return AnswerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.view.answer_body_text.text = answerList[position].body

        holder.view.answer_author_text.text = String.format(
            holder.view.context.getString(R.string.list_text_author),
            answerList[position].ownerName)

        holder.view.setOnClickListener {
            answerClick(position)
        }
    }

    fun refreshList(newList: List<AnswerModel>) {
        answerList.clear()
        answerList.addAll(newList)
        notifyDataSetChanged()
    }

    class AnswerViewHolder(var view: View): RecyclerView.ViewHolder(view)
}