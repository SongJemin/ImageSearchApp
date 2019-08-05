package com.jem.imagesearchapp.UI.Main.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jem.imagesearchapp.R
import com.jem.imagesearchapp.UI.Main.MainActivity
import com.jem.imagesearchapp.Util.DB.DBSearchHelper

class SearchDataHistoryAdapter (ctx: Context, val mainActivity: MainActivity, private var searchData : ArrayList<String>) : RecyclerView.Adapter<SearchDataHistoryViewHolder>(){

    private lateinit var onItemClick : View.OnClickListener
    var searchDbHelper = DBSearchHelper(ctx)

    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchDataHistoryViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_data, parent, false)
        mainView.setOnClickListener(onItemClick)
        return SearchDataHistoryViewHolder(mainView)
    }

    override fun getItemCount(): Int = searchData.size

    override fun onBindViewHolder(holder: SearchDataHistoryViewHolder, position: Int) {

        holder.searchDataName.text = searchData[position]

        holder.searchDataDelete.setOnClickListener {

            var keyword = searchData[position]
            mainActivity.setKeyword(keyword)

            searchData.removeAt(position)
            mainActivity.deleteKeyword(keyword,searchDbHelper)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, searchData.size)
        }
    }
}