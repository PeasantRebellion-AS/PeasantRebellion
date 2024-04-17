package com.peasantrebellion.model

interface Leaderboard {
    fun addHighScoreEntry(
        name: String,
        score: Long,
    )

    fun loadTopHighScores(
        amount: Long,
        onLoaded: (List<Pair<String, Long>>) -> Unit,
    )
}
