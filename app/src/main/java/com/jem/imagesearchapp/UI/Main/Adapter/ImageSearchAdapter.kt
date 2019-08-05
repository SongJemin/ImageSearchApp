package com.jem.imagesearchapp.UI.Main.Adapter

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.jem.imagesearchapp.Data.Model.ImageData
import com.jem.imagesearchapp.R

class ImageSearchAdapter(var requestManager: RequestManager) : RecyclerView.Adapter<ImageSearchViewHolder>() {

    private lateinit var onItemClick : View.OnClickListener
    private val imgDataArr: ArrayList<ImageData> = ArrayList()

    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSearchViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_view, parent, false)
        mainView.setOnClickListener(onItemClick)
        return ImageSearchViewHolder(mainView)
    }

    override fun getItemCount(): Int = imgDataArr.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: ImageSearchViewHolder, position: Int) {
        requestManager.load(imgDataArr[position].image_url).into(holder.backgroundImg)

    }

    fun update(imgDataArr: ArrayList<ImageData>) {
        this.imgDataArr.clear()
        this.imgDataArr.addAll(imgDataArr)
        notifyDataSetChanged()
    }
}