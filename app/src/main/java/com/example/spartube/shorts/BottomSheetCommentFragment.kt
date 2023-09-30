package com.example.spartube.shorts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spartube.data.service.RetrofitModule
import com.example.spartube.databinding.ShortsCommentBinding
import com.example.spartube.shorts.recyclerviewutil.CommentBindingModel
import com.example.spartube.shorts.recyclerviewutil.CommentSetBindingModel
import com.example.spartube.shorts.recyclerviewutil.CommentsAdapter
import com.example.spartube.shorts.recyclerviewutil.reply.ReplyAdapter
import com.example.spartube.util.Converter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomSheetCommentFragment : BottomSheetDialogFragment() {
    private var _binding: ShortsCommentBinding? = null
    private val binding: ShortsCommentBinding
        get() = _binding!!
    private val commentsAdapter by lazy {
        CommentsAdapter(
            onClickReply = { model ->
                showReplyView(model)
            }
        )
    }
    private val replyAdapter by lazy {
        ReplyAdapter()
    }
    private var token: String? = null
    private lateinit var videoId: String
    private val commentsSetList = arrayListOf<CommentSetBindingModel>()
    private val endScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) =
                with(binding) {
                    super.onScrollStateChanged(recyclerView, newState)
                    // 스크롤이 최하단일 떄
                    if (!shortsCommentsRecyclerView.canScrollVertically(1)) {
                        progressBar.isVisible = true
                        getMoreComments(videoId, token)
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ShortsCommentBinding.inflate(layoutInflater)
        videoId = arguments?.getString("videoId", "").toString()
        return binding.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            initViews(bottomSheetDialog)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() = with(binding) {
        shortsCommentsRecyclerView.run {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = commentsAdapter
            addOnScrollListener(endScrollListener)
        }
    }

    private fun initViews(dialog: BottomSheetDialog) = with(binding) {
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        shortsCommentCloseImageView.setOnClickListener {
            dismiss()
        }
    }

    private fun showReplyView(model: CommentSetBindingModel) = with(binding) {
        val startAnimation =
            TranslateAnimation(shortsReplyRecyclerView.width.toFloat(), 0f, 0f, 0f).apply {
                duration = 300
            }
        val endAnimation =
            TranslateAnimation(0f, shortsReplyRecyclerView.width.toFloat(), 0f, 0f).apply {
                duration = 300
            }
        val replyModel = mappingModel(model)
        titleTextView.text = "답글"
        backBtn.run {
            isVisible = true
            setOnClickListener {
                backBtn.isVisible = false
                titleTextView.text = "댓글"
                shortsReplyRecyclerView.run {
                    animation = endAnimation
                    isVisible = false
                }
            }
        }
        shortsReplyRecyclerView.run {
            animation = startAnimation
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = replyAdapter.apply {
                addItems(replyModel)
            }
            isVisible = true
        }

    }

    private fun mappingModel(model: CommentSetBindingModel): List<CommentBindingModel> {
        val list = arrayListOf<CommentBindingModel>()
        list.add(model.toCommentBindingModel())
        model.repliesFromTop?.comments?.forEach { reply ->
            val snippet = reply.snippet
            list.add(
                CommentBindingModel(
                    userImageUrl = snippet.authorProfileImageUrl,
                    userName = snippet.authorDisplayName,
                    publishedAt = Converter.getDateFromTimestampWithFormat(snippet.publishedAt),
                    description = snippet.textDisplay,
                    likeCount = snippet.likeCount,
                )
            )
        }
        return list
    }

    private fun getMoreComments(videoId: String, nextPageToken: String?) = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            commentsSetList.clear()
            val responseComments = RetrofitModule.getCommentsOfShorts(videoId, nextPageToken)
            responseComments.body()?.let { comments ->
                comments.items.forEach { comment ->
                    commentsSetList.add(
                        CommentSetBindingModel(
                            comment.snippet,
                            comment.replies,
                            ViewType.OTHER.order,
                        )

                    )
                    commentsSetList.first().viewType = ViewType.TOP.order
                }
                requireActivity().runOnUiThread {
                    addComments(commentsSetList, comments.nextPageToken)
                    progressBar.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun addComments(commentsSetList: List<CommentSetBindingModel>, nextPageToken: String?) {
        println(commentsSetList)
        when (nextPageToken) {
            null -> commentsAdapter.addItems(commentsSetList)
            else -> commentsAdapter.addMoreItems(commentsSetList)
        }
        token = nextPageToken
    }

    companion object {
        const val TAG = "BOTTOM_SHEET_FRAGMENT"
        fun newInstance() = BottomSheetCommentFragment()
    }
}

enum class ViewType(val order: Int) {
    TOP(0), OTHER(1)
}