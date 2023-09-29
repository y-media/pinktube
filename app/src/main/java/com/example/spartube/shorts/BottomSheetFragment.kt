package com.example.spartube.shorts

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spartube.databinding.ShortsCommentBinding
import com.example.spartube.shorts.recyclerview.CommentsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment(
    private val context: Context
) : BottomSheetDialogFragment() {
    private var _binding: ShortsCommentBinding? = null
    private val binding: ShortsCommentBinding
        get() = _binding!!
    private val commentsAdapter by lazy {
        CommentsAdapter()
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun addComments(commentsSetList: List<CommentSetBindingModel>) {
        commentsAdapter.addItems(commentsSetList.map { it.toCommentBindingModel() })
    }

    companion object {
        const val TAG = "BOTTOM_SHEET_FRAGMENT"
    }
}