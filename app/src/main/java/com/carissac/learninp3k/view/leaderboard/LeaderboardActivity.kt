package com.carissac.learninp3k.view.leaderboard

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.data.remote.response.LeaderboardResponse
import com.carissac.learninp3k.databinding.ActivityLeaderboardBinding
import kotlinx.coroutines.launch

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private var userId: Int? = null

    private val leaderboardViewModel: LeaderboardViewModel by viewModels {
        LeaderboardViewModelFactory(Injection.provideLeaderboardRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.leaderboard) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        leaderboardViewModel.getLeaderboard()

        leaderboardViewModel.viewModelScope.launch {
            userId = leaderboardViewModel.getUserId()

            leaderboardAdapter = LeaderboardAdapter(userId ?: 0)
            binding.rvUserLeaderboard.apply {
                layoutManager = LinearLayoutManager(this@LeaderboardActivity)
                adapter = leaderboardAdapter
            }

            observeViewModel()
        }

        leaderboardViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun observeViewModel() {
        leaderboardViewModel.topPlayerResult.observe(this) { response ->
            response.onSuccess { topPlayers ->
                displayTopPlayers(topPlayers)
            }.onFailure {
                showToast("Gagal mengambil data leaderboard")
            }
        }

        leaderboardViewModel.underTopPlayerResult.observe(this) { response ->
            response.onSuccess { underTopPlayers ->
                leaderboardAdapter.submitList(underTopPlayers)
            }.onFailure {
                showToast("Gagal mengambil data leaderboard")
            }
        }
    }

    private fun displayTopPlayers(topPlayers: List<LeaderboardResponse>) {
        val topViews = listOf(
            Triple(binding.ivUserRank1, binding.tvUsernameRank1, binding.tvTotalPointRank1),
            Triple(binding.ivUserRank2, binding.tvUsernameRank2, binding.tvTotalPointRank2),
            Triple(binding.ivUserRank3, binding.tvUsernameRank3, binding.tvTotalPointRank3)
        )

        for(i in topPlayers.indices) {
            val user = topPlayers[i]
            val (avatar, username, points) = topViews[i]

            if(user.userId == userId) {
                username.text = "Anda"
                username.setTypeface(null, Typeface.BOLD)
                username.setTextAppearance(R.style.LeaderboardStyleHighlightedText)
            } else {
                username.text = user.userName
            }

            points.text = user.totalPoint.toString()

            Glide.with(this)
                .load(user.avatarImg)
                .centerCrop()
                .into(avatar)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}