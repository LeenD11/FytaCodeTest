package com.leen.fytacodetest.activities.results

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.leen.fytacodetest.R
import com.leen.fytacodetest.networking.resultdataclasses.Result
import kotlin.math.roundToInt


class ResultsAdapter(private val context: Context, private val resultsList: List<Result>) : RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {

    inner class ResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //create variables to refer to from the activity layout
        val resultTitle: TextView = view.findViewById(R.id.txt_result_title)
        val resultSubtitle: TextView = view.findViewById(R.id.txt_result_subtitle)
        val resultScore: TextView = view.findViewById(R.id.txt_result_score)
        val resultImage: ImageView = view.findViewById(R.id.img_result)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        return ResultsViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        //create a result item at the position for easier reference
        val resultItem = resultsList[position]
        //set result title and subtitle
        val resultTitle = resultItem.species.scientificName
        val resultImage = resultItem.images[0].url
        holder.resultTitle.text = resultTitle
        holder.resultSubtitle.text = resultTitle
        //multiplies the score by 100 and round it to get the score as an integer.
        holder.resultScore.text = resultItem.score.times(100).roundToInt().toString() + "%"
        //load image uri using glide into the the list
        Glide.with(context).load(resultImage.m).into(holder.resultImage)
        //show dialog when item is clicked in recycler view
        holder.itemView.setOnClickListener {
            showDialog(resultTitle, resultImage.m)
        }
    }

    override fun getItemCount(): Int {
        //limited the list size to the first 2 results only
        return if (resultsList.size > 2) {
            2
        } else {
            resultsList.size
        }
    }

    //create result dialog
    private fun showDialog(resultTitle: String, resultImage: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_image)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        //set transparent background to show the washed black color background with rounded corners.
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        val dialogImage = dialog.findViewById(R.id.img_dialog) as ImageView
        val dialogTitle = dialog.findViewById(R.id.txt_dialog_title) as TextView
        dialogTitle.text = resultTitle
        Glide.with(context)
            .load(resultImage)
            //had to add corner radius because image is not fitting properly in the image view
            //might not be so efficient, should find a better way.
            .apply(RequestOptions.bitmapTransform(GranularRoundedCorners(0F,0F, 100.0F, 100.0F)))
            .into(dialogImage)
        dialog.show()
    }
}
