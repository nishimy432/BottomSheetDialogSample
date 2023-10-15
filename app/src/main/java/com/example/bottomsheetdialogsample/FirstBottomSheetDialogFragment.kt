package com.example.bottomsheetdialogsample

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.example.bottomsheetdialogsample.databinding.FragmentFirstBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.R

class FirstBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFirstBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBottomSheetDialogBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val fullScreenHeight = getFullScreenHeight()
        val displayHeight = fullScreenHeight - getStatusBarHeight()

        bottomSheetDialog.setOnShowListener { dialog ->
            (dialog as? BottomSheetDialog)?.findViewById<FrameLayout>(R.id.design_bottom_sheet)
                ?.let {
                    BottomSheetBehavior.from(it).apply {
                        peekHeight = (displayHeight * 0.5).toInt()
                        maxHeight = displayHeight
                        state = BottomSheetBehavior.STATE_COLLAPSED

                        addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                            override fun onStateChanged(bottomSheet: View, newState: Int) {}

                            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                                val currentBottomSheetHeight = fullScreenHeight - bottomSheet.top
                                val currentFooterHeight =
                                    currentBottomSheetHeight - binding.footerView.height

                                binding.footerView.y = currentFooterHeight.toFloat()
                            }
                        }.apply {
                            binding.root.post { onSlide(binding.root.parent as View, 0f) }
                        })
                    }
                }
        }

        return bottomSheetDialog
    }

    private fun getFullScreenHeight(): Int {
        val manager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = manager.currentWindowMetrics
            metrics.bounds.height()
        } else {
            val metrics = DisplayMetrics()
            manager.defaultDisplay.getRealMetrics(metrics)
            metrics.heightPixels
        }
    }

    private fun getStatusBarHeight(): Int {
        val outRect = Rect()
        requireActivity().window.decorView.getWindowVisibleDisplayFrame(outRect)
        return outRect.top
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): FirstBottomSheetDialogFragment = FirstBottomSheetDialogFragment()
    }
}