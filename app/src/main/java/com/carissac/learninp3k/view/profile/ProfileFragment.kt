package com.carissac.learninp3k.view.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.data.remote.response.UserBadgeResponse
import com.carissac.learninp3k.databinding.FragmentProfileBinding
import com.carissac.learninp3k.databinding.PopUpBadgeBinding
import com.carissac.learninp3k.view.leaderboard.LeaderboardActivity
import com.carissac.learninp3k.view.leaderboard.LeaderboardViewModel
import com.carissac.learninp3k.view.leaderboard.LeaderboardViewModelFactory
import com.carissac.learninp3k.view.setting.SettingActivity
import com.carissac.learninp3k.view.utils.GridSpacingItemDecoration
import com.carissac.learninp3k.view.utils.formatDate

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var badgeAdapter: BadgeAdapter
    private var currentPopupWindow: PopupWindow? = null

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(Injection.provideUserRepository(requireContext()))
    }

    private val leaderboardViewModel: LeaderboardViewModel by viewModels {
        LeaderboardViewModelFactory(Injection.provideLeaderboardRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.userLeaderboardResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { profile ->
                binding.tvProfileUsername.text = profile.userName
                binding.tvTotalBadge.text = profile.badgeCount?.toString() ?: "0"
                binding.tvTotalPoint.text = profile.totalPoint?.toString() ?: "0"

                Glide.with(this@ProfileFragment)
                    .load(profile.avatarImg)
                    .centerCrop()
                    .into(binding.ivProfileUser)
            }.onFailure {
                showToast("Gagal mengambil profil, silahkan mencoba kembali")
            }
        }

        profileViewModel.getUserLeaderboard()

        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        setupBadgeList()

        leaderboardViewModel.userBadgeResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { badgeDetail ->
                showBadgePopUp(badgeDetail)
            }.onFailure {
                showToast("Gagal mengambil detail badge")
            }
        }

        binding.ivIcSetting.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        binding.btnLeaderboard.setOnClickListener {
            val intent = Intent(requireContext(), LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupBadgeList() {
        badgeAdapter = BadgeAdapter { id ->
            leaderboardViewModel.getUserBadgeById(id)
        }

        binding.rvBadge.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = badgeAdapter
            addItemDecoration(GridSpacingItemDecoration(16))
        }

        leaderboardViewModel.listBadgeResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { listBadge ->
                badgeAdapter.submitList(listBadge)
            }.onFailure {
                showToast("Gagal mengambil data badge")
            }
        }

        leaderboardViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        leaderboardViewModel.getAllUserBadge()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showBadgePopUp(badgeDetail: UserBadgeResponse) {
        if (!isAdded || activity == null) return // Prevent crash if fragment is detached
        if (currentPopupWindow != null) {
            currentPopupWindow?.dismiss()
            return
        } // Avoid duplicate popups

        val bindingPopUp = PopUpBadgeBinding.inflate(layoutInflater)
        val viewPopUp = bindingPopUp.root
        val windowPopUp = PopupWindow(
            viewPopUp,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            isOutsideTouchable = true
            setBackgroundDrawable(null)
            view?.post { // Ensure popup is shown only when the UI is ready
                if (isAdded) {
                    dimBehind()
                    showAtLocation(view, Gravity.CENTER, 0, 0)
                }
            }
            setOnDismissListener {
                Log.d("PopupDebug", "Popup dismissed")
                currentPopupWindow = null
                clearDim()
            }
        }

        bindingPopUp.tvBadgeName.text = badgeDetail.badgeName
        val badgeCategory = if(badgeDetail.badgeCategory.equals("Course", ignoreCase = true)) "Kelas" else badgeDetail.badgeCategory
        bindingPopUp.tvBadgeCategory.text = badgeCategory
        bindingPopUp.tvBadgeDesc.text = badgeDetail.badgeDesc
        bindingPopUp.tvBadgeDate.text = formatDate(badgeDetail.createdAt ?: "")

        Glide.with(requireContext())
            .load(badgeDetail.badgeImg)
            .centerCrop()
            .into(bindingPopUp.ivBadge)

        bindingPopUp.btnClose.setOnClickListener {
            Log.d("PopupDebug", "Popup dismissed btn close")
            windowPopUp.dismiss()
            clearDim()
            currentPopupWindow = null
        }

        currentPopupWindow = windowPopUp
    }

    private fun dimBehind() {
        val window = requireActivity().window
        val layoutParams = window.attributes
        layoutParams.dimAmount = 0.5f
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) // Correctly add the flag
        window.attributes = layoutParams
    }

    private fun clearDim() {
        val window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) // Properly clear the flag
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getUserLeaderboard()
    }

    override fun onPause() {
        super.onPause()
        currentPopupWindow?.dismiss()
        clearDim()
        currentPopupWindow = null
    }
}