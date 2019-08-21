package com.tombecker.acculynxchallenge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.tombecker.acculynxchallenge.R
import com.tombecker.acculynxchallenge.model.QuestionModel
import com.tombecker.acculynxchallenge.util.AppConstants.Companion.QUESTION_DATE_FORMAT
import com.tombecker.acculynxchallenge.util.AppUtils
import com.tombecker.acculynxchallenge.view.QuestionListFragmentDirections
import kotlinx.android.synthetic.main.question_list_item.view.*

class QuestionListAdapter(private val questionList: ArrayList<QuestionModel>): RecyclerView.Adapter<QuestionListAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.question_list_item, parent, false)
        return QuestionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.view.question_title_text.text = questionList[position].title

        holder.view.question_owner_text.text = String.format(
            holder.view.context.getString(R.string.list_text_author),
            questionList[position].ownerName)

        holder.view.question_creationdate_text.text = String.format(
            holder.view.context.getString(R.string.list_text_creationdate),
            AppUtils.getDateFromMillis(questionList[position].creationDate, QUESTION_DATE_FORMAT))

        holder.view.question_numanswers_text.text = String.format(
            holder.view.context.getString(R.string.list_text_numanswers),
            questionList[position].answerCount.toString())

        holder.view.setOnClickListener {
            handleListClick(it, position)
        }
    }

    private fun handleListClick(view: View, position: Int) {
        val action = QuestionListFragmentDirections.actionListToQuiz()
        questionList[position].questionId?.let {
            action.questionUuid = it
        }
        Navigation.findNavController(view).navigate(action)
    }

    fun refreshList(newList: List<QuestionModel>) {
        questionList.clear()
        questionList.addAll(newList)
        notifyDataSetChanged()
    }

    class QuestionViewHolder(var view: View): RecyclerView.ViewHolder(view)

}