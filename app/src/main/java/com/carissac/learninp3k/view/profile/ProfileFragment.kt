package com.carissac.learninp3k.view.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.FragmentProfileBinding
import com.carissac.learninp3k.view.leaderboard.LeaderboardActivity
import com.carissac.learninp3k.view.leaderboard.LeaderboardViewModel
import com.carissac.learninp3k.view.leaderboard.LeaderboardViewModelFactory
import com.carissac.learninp3k.view.setting.SettingActivity
import com.carissac.learninp3k.view.utils.GridSpacingItemDecoration

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var badgeAdapter: BadgeAdapter

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
        badgeAdapter = BadgeAdapter { badgeId ->
            val badgeDialog = BadgeDialogFragment.newInstance(badgeId)
            badgeDialog.show(childFragmentManager, BadgeDialogFragment.TAG)
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
}