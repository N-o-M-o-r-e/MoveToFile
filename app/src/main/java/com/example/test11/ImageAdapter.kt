package com.example.test11

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class ImageAdapter(private val context: Context, private val imageList: ArrayList<String>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var imagePaths: ArrayList<String> = ArrayList()
    private var sortType: SortType = SortType.TIME

    fun setSortType(type: SortType) {
        sortType = type
        sortImageList()
    }

    fun sortImageList() {
        when (sortType) {
            SortType.TIME -> {
                // Sắp xếp theo thời gian
                imageList.sortByDescending { File(it).lastModified() }
            }
            SortType.NAME -> {
                // Sắp xếp theo tên
                imageList.sortBy { File(it).name }
            }
            SortType.SIZE -> {
                // Sắp xếp theo kích thước
                imageList.sortByDescending { File(it).length() }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = imageList[position]
        Glide.with(context)
            .load(imagePath)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun updateImageList(newImagePaths: ArrayList<String>) {
        imagePaths.clear()
        imagePaths.addAll(newImagePaths)
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            imageView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val imagePath = imageList[position]
                    itemClickListener?.onItemClick(imagePath)
                }
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(imagePath: String)
    }
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    enum class SortType{ TIME, NAME, SIZE}

}