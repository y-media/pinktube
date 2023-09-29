package com.example.spartube.shorts

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spartube.databinding.ShortsCommentBinding
import com.example.spartube.shorts.recyclerviewutil.CommentBindingModel
import com.example.spartube.shorts.recyclerviewutil.CommentSetBindingModel
import com.example.spartube.shorts.recyclerviewutil.CommentsAdapter
import com.example.spartube.shorts.recyclerviewutil.reply.ReplyAdapter
import com.example.spartube.util.Converter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCommentFragment(
    private val context: Context
) : BottomSheetDialogFragment() {
    private var _binding: ShortsCommentBinding? = null
    private val binding: ShortsCommentBinding
        get() = _binding!!
    private val commentsAdapter by lazy {
        CommentsAdapter(
            onClickReply = { model ->
                showReplyFragment(model)
            }
        )
    }
    private val replyAdapter by lazy {
        ReplyAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ShortsCommentBinding.inflate(layoutInflater)
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

    private fun showReplyFragment(model: CommentSetBindingModel) = with(binding) {
        val startAnimation =
            TranslateAnimation(shortsReplyRecyclerView.width.toFloat(), 0f, 0f, 0f).apply {
                duration = 500
            }
        val endAnimation =
            TranslateAnimation(0f, shortsReplyRecyclerView.width.toFloat(), 0f, 0f).apply {
                duration = 500
            }
        val replyModel = mappingModel(model)
        titleTextView.text = "답글"
        backBtn.run {
            isVisible = true
            setOnClickListener {
                backBtn.isVisible = false
                titleTextView.text = "댓글"
                shortsReplyRecyclerView.run {
                    this.animation = endAnimation
                    isVisible = false
                }
            }
        }
        shortsReplyRecyclerView.run {
            this.animation = startAnimation
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun addComments(commentsSetList: List<CommentSetBindingModel>) {
        commentsAdapter.addItems(commentsSetList)
    }

    companion object {
        const val TAG = "BOTTOM_SHEET_FRAGMENT"
        fun newInstance(context: Context) = BottomSheetCommentFragment(context)
    }
}

enum class ViewType(val order: Int) {
    TOP(0), OTHER(1)
}