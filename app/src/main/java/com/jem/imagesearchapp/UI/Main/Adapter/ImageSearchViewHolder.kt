package com.jem.imagesearchapp.UI.Main.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.jem.imagesearchapp.R

class ImageSearchViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var backgroundImg: ImageView = itemView!!.findViewById(R.id.item_background_img)
}