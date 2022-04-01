package com.leen.fytacodetest.activities.results

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.leen.fytacodetest.R
import com.leen.fytacodetest.databinding.ActivityResultsBinding
import com.leen.fytacodetest.networking.resultdataclasses.Result
import com.leen.fytacodetest.utils.Constants

class ResultsActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityResultsBinding
    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var results: ArrayList<Result>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        viewBinding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //get results as array list from main activity after api call
        results = intent.getSerializableExtra(Constants.INTENT_KEY) as ArrayList<Result>

        viewBinding.btnBack.setOnClickListener { onBackPressed() }
        viewBinding.btnRetakePhoto.setOnClickListener { onBackPressed() }

        //init the adapter for results
        resultsAdapter = ResultsAdapter(this, results)
        viewBinding.resultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ResultsActivity)
            adapter = resultsAdapter
        }
    }
}
