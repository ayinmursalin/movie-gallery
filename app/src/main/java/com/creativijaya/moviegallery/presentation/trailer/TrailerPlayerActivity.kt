package com.creativijaya.moviegallery.presentation.trailer

import android.os.Bundle
import android.widget.Toast
import com.creativijaya.moviegallery.BuildConfig
import com.creativijaya.moviegallery.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment

class TrailerPlayerActivity : YouTubeBaseActivity(),
    YouTubePlayer.OnInitializedListener {

    private val videoKey: String by lazy {
        intent?.getStringExtra("video_key").orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer_player)

        val playerFragment = fragmentManager.findFragmentById(
            R.id.player_fragment_trailer
        ) as? YouTubePlayerFragment

        playerFragment?.initialize(BuildConfig.YOUTUBE_API_KEY, this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        if (!wasRestored) {
            player?.cueVideo(videoKey)
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        result: YouTubeInitializationResult?
    ) {
        Toast.makeText(this, "Error ${result?.name}", Toast.LENGTH_SHORT).show()
    }
}
