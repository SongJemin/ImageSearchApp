package com.jem.imagesearchapp.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jem.imagesearchapp.R
import com.jem.imagesearchapp.data.model.SearchData

class SearchAdapter(val itemDeleteClick: (SearchData) -> Unit, val itemAllClick: (SearchData) -> Unit)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private var searchDatas: List<SearchData> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_data, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchDatas.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(searchDatas[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val contentTv = itemView.findViewById<TextView>(R.id.item_search_data_tv)
        private val deleteBtn = itemView.findViewById<ImageView>(R.id.item_search_delete_iv)

        // 바인딩
        fun bind(search: SearchData) {
            contentTv.text = search.keyword

            // 최근 검색어 특정 데이터 삭제 버튼
            deleteBtn.setOnClickListener {
                itemDeleteClick(search)
            }

            // 리스트 하나 선택
            itemView.setOnClickListener {
                itemAllClick(search)
            }
        }
    }

    // 데이터 갱신
    fun setSearcgDatas(searchDatas: List<SearchData>) {
        this.searchDatas = searchDatas
        notifyDataSetChanged()
    }
}