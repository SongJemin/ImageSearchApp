package com.jem.imagesearchapp.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.jem.imagesearchapp.R
import com.jem.imagesearchapp.data.model.ImageData

class ImageSearchAdapter(val imageClick: (ImageData) -> Unit, var requestManager: RequestManager)
    : RecyclerView.Adapter<ImageSearchAdapter.ViewHolder>() {
    private var images: ArrayList<ImageData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(images[position], requestManager)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var backgroundImg: ImageView = itemView!!.findViewById(R.id.item_background_img)

        // 바인딩
        fun bind(image: ImageData, requestManager: RequestManager) {
            requestManager.load(image.image_url).into(backgroundImg)

            // 이미지 클릭 시
            itemView.setOnClickListener {
                imageClick(image)
            }
        }
    }

    // 데이터 갱신
    fun update(imgDataArr: ArrayList<ImageData>) {
        this.images.clear()
        this.images.addAll(imgDataArr)
        notifyDataSetChanged()
    }
}