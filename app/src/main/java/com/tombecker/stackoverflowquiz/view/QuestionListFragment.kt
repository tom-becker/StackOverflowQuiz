package com.tombecker.stackoverflowquiz.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.tombecker.stackoverflowquiz.R
import com.tombecker.stackoverflowquiz.adapter.QuestionListAdapter
import com.tombecker.stackoverflowquiz.viewmodel.QuestionListViewModel
import kotlinx.android.synthetic.main.question_list_fragment.*

class QuestionListFragment : Fragment() {

    private lateinit var viewModel: QuestionListViewModel

    private val questionListAdapter = QuestionListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.question_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(QuestionListViewModel::class.java)
        viewModel.refresh()

        initView()
        subscribeToViewModel()
    }

    private fun initView() {
        question_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = questionListAdapter
        }

        refreshLayout.setOnRefreshListener {
            question_list.visibility = View.GONE
            list_error_text.visibility = View.GONE
            refreshLayout.isRefreshing = false
            viewModel.refresh(true)
        }
    }

    private fun subscribeToViewModel() {
        viewModel.questionListLiveData.observe(this, Observer { list ->
            list?.let {
                if(list.isNotEmpty()) {
                    question_list.visibility = View.VISIBLE
                    questionListAdapter.refreshList(list)
                } else {
                    question_list.visibility = View.GONE
                    list_error_text.visibility = View.VISIBLE
                }
                refreshLayout.isRefreshing = false
            }
        })

        viewModel.loadErrorLiveData.observe(this, Observer { isError ->
            isError?.let {
                list_error_text.visibility = if(it) View.VISIBLE else View.GONE
                refreshLayout.isRefreshing = false
            }
        })

        viewModel.questionsAreLoading.observe(this, Observer { isLoading ->
            isLoading?.let {
                questions_loading_progress.visibility = if(it) View.VISIBLE else View.GONE
            }
        })
    }

}
