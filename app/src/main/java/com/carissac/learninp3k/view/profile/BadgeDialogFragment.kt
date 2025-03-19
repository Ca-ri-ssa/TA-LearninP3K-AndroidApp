package com.carissac.learninp3k.view.profile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.FragmentBadgeDialogBinding
import com.carissac.learninp3k.view.leaderboard.LeaderboardViewModel
import com.carissac.learninp3k.view.leaderboard.LeaderboardViewModelFactory
import com.carissac.learninp3k.view.utils.formatDate

@RequiresApi(Build.VERSION_CODES.O)
class BadgeDialogFragment : DialogFragment() {
    private var _binding: FragmentBadgeDialogBinding? = null
    private val binding get() = _binding!!

    private val leaderboardViewModel: LeaderboardViewModel by viewModels {
        LeaderboardViewModelFactory(Injection.provideLeaderboardRepository(requireContext()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBadgeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val badgeId = arguments?.getInt("BADGE_ID") ?: -1

        leaderboardViewModel.getUserBadgeById(badgeId)
        observeBadgeDetail()

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun observeBadgeDetail() {
        leaderboardViewModel.userBadgeResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                binding.tvBadgeName.text = response.badgeName
                binding.tvBadgeDate.text = formatDate(response.createdAt ?: "")
                val badgeCategory = if(response.badgeCategory.equals("Course", ignoreCase = true)) "Kelas" else response.badgeCategory
                binding.tvBadgeCategory.text = badgeCategory
                binding.tvBadgeDesc.text = response.badgeDesc

                Glide.with(requireContext())
                    .load(response.badgeImg)
                    .centerCrop()
                    .into(binding.ivBadge)
            }.onFailure {
                showToast("Gagal memuat detail badge")
            }
        }

        leaderboardViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "BadgeDialogFragment"

        fun newInstance(badgeId: Int): BadgeDialogFragment {
            return BadgeDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt("BADGE_ID", badgeId)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}