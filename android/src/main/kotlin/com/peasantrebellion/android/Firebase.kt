package com.peasantrebellion.android

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.peasantrebellion.model.Leaderboard

class Firebase : Leaderboard {
    private val db = FirebaseFirestore.getInstance()

    override fun addHighScoreEntry(
        name: String,
        score: Long,
    ) {
        db.collection("highscores").add(mapOf("name" to name, "score" to score))
    }

    override fun loadTopHighScores(
        amount: Long,
        onLoaded: (List<Pair<String, Long>>) -> Unit,
    ) {
        db.collection("highscores").orderBy("score", Query.Direction.DESCENDING).limit(amount).get()
            .addOnSuccessListener {
                it.documents.map { doc -> doc.data }
                    .map { data -> data?.get("name") to data?.get("score") }
                    .map { (name, score) -> name as String to score as Long }
                    .let { entry -> onLoaded(entry) }
            }
    }
}
