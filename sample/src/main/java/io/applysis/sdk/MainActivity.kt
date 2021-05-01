package io.applysis.sdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->


            GlobalScope.launch {
                val feedback = Feedback(
                    "my text",
                    "title here",
                    date = Date(),
                    rating = 5,
                    author = "Ioane Yo",
                    region = "Tbilisi/Geo",
                    version = "1.2.3",
                )

                val result = Applysis(
                    TODO("your api-key"),
                    debugMode = true
                ).submitFeedback(feedback)

                Snackbar.make(view, result.toString(), Snackbar.LENGTH_LONG).show()
            }

        }
    }
}