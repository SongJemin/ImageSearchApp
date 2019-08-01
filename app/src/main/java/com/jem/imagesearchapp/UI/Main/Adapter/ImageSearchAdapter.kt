package com.jem.imagesearchapp.UI.Main.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.jem.imagesearchapp.Data.Model.ImageData
import com.jem.imagesearchapp.R

class ImageSearchAdapter(context: Context, private var imgDataArr: ArrayList<ImageData>, var requestManager: RequestManager) : RecyclerView.Adapter<ImageSearchViewHolder>() {

    val  mContext : Context = context
    private lateinit var onItemClick : View.OnClickListener

    fun setOnItemClickListener1(l : View.OnClickListener){
        onItemClick = l
    }

    //내가 쓸 뷰홀더가 뭔지를 적어준다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSearchViewHolder {
        val mainView1 : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_view, parent, false)
//        mainView1.setOnClickListener(onItemClick)
        return ImageSearchViewHolder(mainView1)
    }

    override fun getItemCount(): Int = imgDataArr.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: ImageSearchViewHolder, position: Int) {
        requestManager.load(imgDataArr[position].image_url).into(holder.backgroundImg)
    }
}