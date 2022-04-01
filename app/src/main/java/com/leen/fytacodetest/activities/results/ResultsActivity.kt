package com.leen.fytacodetest.activities.results

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.leen.fytacodetest.R
import com.leen.fytacodetest.databinding.ActivityResultsBinding
import com.leen.fytacodetest.networking.resultdataclasses.Result
import com.leen.fytacodetest.utils.Constants

class ResultsActivity : AppCompatActivity() {

    //0-25 red
    //26-50 orange
    //51-75 yellow
    //76-100 green

    private lateinit var viewBinding: ActivityResultsBinding
    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var results: ArrayList<Result>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        viewBinding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        results = intent.getSerializableExtra(Constants.INTENT_KEY) as ArrayList<Result>
        Log.d(TAG, "onCreate: $results")

        viewBinding.btnBack.setOnClickListener { onBackPressed() }

        resultsAdapter = ResultsAdapter(this, results)
        viewBinding.resultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ResultsActivity)
            adapter = resultsAdapter
        }
    }

    companion object {
        private const val TAG = "ResultsActivity"
    }
}
