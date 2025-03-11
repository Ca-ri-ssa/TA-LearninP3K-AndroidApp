package com.carissac.learninp3k.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.FragmentProfileBinding
import com.carissac.learninp3k.view.leaderboard.LeaderboardActivity
import com.carissac.learninp3k.view.setting.SettingActivity

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(Injection.provideUserRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

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

        binding.ivIcSetting.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        binding.btnLeaderboard.setOnClickListener {
            val intent = Intent(requireContext(), LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}