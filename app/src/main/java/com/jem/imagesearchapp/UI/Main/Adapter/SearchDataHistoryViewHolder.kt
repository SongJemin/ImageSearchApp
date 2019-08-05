package com.jem.imagesearchapp.UI.Main.Adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jem.imagesearchapp.R

class SearchDataHistoryViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var searchDataName: TextView = itemView!!.findViewById(R.id.item_search_data_tv)
    var searchDataDelete : ImageView = itemView!!.findViewById(R.id.item_search_delete_iv)
}