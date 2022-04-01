package com.leen.fytacodetest.activities.results

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leen.fytacodetest.R
import com.leen.fytacodetest.networking.resultdataclasses.Result
import kotlin.math.roundToInt

class ResultsAdapter(private val context: Context, private val resultsList: List<Result>) : RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {

    inner class ResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resultTitle: TextView = view.findViewById(R.id.txt_result_title)
        val resultSubtitle: TextView = view.findViewById(R.id.txt_result_subtitle)
        val resultScore: TextView = view.findViewById(R.id.txt_result_score)
        val resultImage: ImageView= view.findViewById(R.id.img_result)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        return ResultsViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        holder.resultTitle.text = resultsList[position].species.scientificName
        holder.resultSubtitle.text = resultsList[position].species.scientificName
        holder.resultScore.text =resultsList[position].score.times(100).roundToInt().toString() + "%"
        Glide.with(context)
            .load(resultsList[position].images[0].url.s)
            .into(holder.resultImage)

        //        holder.itemView.setOnClickListener {
        //            onResultClickListener.invoke(resultsList[position])
        //        }
    }

    override fun getItemCount(): Int {
        return if(resultsList.size > 2){
            2
        } else {
            resultsList.size
        }
    }
}