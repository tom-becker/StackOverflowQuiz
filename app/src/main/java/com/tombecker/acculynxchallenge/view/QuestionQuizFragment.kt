package com.tombecker.acculynxchallenge.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

import com.tombecker.acculynxchallenge.R
import com.tombecker.acculynxchallenge.adapter.AnswerListAdapter
import com.tombecker.acculynxchallenge.viewmodel.QuestionQuizViewModel
import kotlinx.android.synthetic.main.question_quiz_fragment.*

class QuestionQuizFragment : Fragment() {

    private lateinit var viewModel: QuestionQuizViewModel

    private lateinit var answerListAdapter: AnswerListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.question_quiz_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: QuestionQuizFragmentArgs by navArgs()

        viewModel = ViewModelProviders.of(this).get(QuestionQuizViewModel::class.java)
        viewModel.fetchQuestion(args.questionUuid)

        initView()
        subscribeToViewModel()
    }

    private fun initView() {
        answerListAdapter = AnswerListAdapter(arrayListOf()) { position ->
            answer_list.layoutManager?.findViewByPosition(position)?.let {
                updateAnswerStyle(it, viewModel.isAnswerCorrect(position))
            }
        }

        quiz_header_container.setOnClickListener {
            AlertDialog.Builder(it.context, R.style.AppTheme)
                .setTitle(viewModel.selectedQuestion.value?.title
                    ?: it.context.getString(R.string.error_getting_question_title_text))
                .setMessage(viewModel.selectedQuestion.value?.body
                    ?: it.context.getString(R.string.error_getting_question_body_text))
                .setPositiveButton(R.string.dialog_close_button) { dialog, _ ->
                    dialog.dismiss()
                }
                .create().show()
        }

        answer_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = answerListAdapter
        }
    }

    private fun updateAnswerStyle(view: View, isCorrect: Boolean) {
        context?.let {
            when(isCorrect) {
                true -> view.setBackgroundColor(ContextCompat.getColor(it, R.color.correct_card_color))
                false -> view.setBackgroundColor(ContextCompat.getColor(it, R.color.incorrect_card_color))
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.selectedQuestion.observe(this, Observer { questionModel ->
            questionModel?.let {
                quiz_question_title.text = it.title
                quiz_header_hint_text.text = context?.getString(R.string.quiz_header_hint)
            }
        })

        viewModel.answerList.observe(this, Observer { list ->
            list?.let {
                answer_list.visibility = View.VISIBLE
                answer_list_error.visibility = View.GONE
                answerListAdapter.refreshList(list)
            }
        })

        viewModel.loadingAnswersError.observe(this, Observer { isError ->
            isError?.let {
                if(it) {
                    answer_list_error.visibility = View.VISIBLE
                    answer_list.visibility = View.GONE
                }
            }
        })

        viewModel.loadingQuestionError.observe(this, Observer { isError ->
            isError?.let {
                if(it) {
                    quiz_header_hint_text.text = context?.getString(R.string.error_getting_question_title_text)
                    quiz_header_container.visibility = View.GONE
                }
            }
        })

        viewModel.answersAreLoading.observe(this, Observer { isLoading ->
            isLoading?.let {
                answers_loading_progress.visibility = if(it) View.VISIBLE else View.GONE
            }
        })
    }
}
