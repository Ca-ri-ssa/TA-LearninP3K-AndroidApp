package com.carissac.learninp3k.view.challenge

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.carissac.learninp3k.databinding.ActivityWeeklyChallengeBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class WeeklyChallengeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeeklyChallengeBinding
    private lateinit var youtubePlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityWeeklyChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.weeklyChallenge) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        setContent()
    }

    private fun setContent() {
        // TODO: Dont forget to make this into detail course
//        youtubePlayerView = binding.vidCourse
//
//        lifecycle.addObserver(youtubePlayerView)
//
//        val options = IFramePlayerOptions.Builder()
//            .controls(1) // Show controls
//            .fullscreen(1) // Enable fullscreen button
//            .build()
//
//        youtubePlayerView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                val videoId = "QNze7amrOCE"
//                youTubePlayer.cueVideo(videoId, 0f)
//            }
//        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    // TODO: Dont forget to make this into detail course
//    override fun onDestroy() {
//        super.onDestroy()
//        youtubePlayerView.release()
//    } this for course vid
}