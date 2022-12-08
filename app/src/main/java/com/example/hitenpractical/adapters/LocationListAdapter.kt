package com.example.hitenpractical.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hitenpractical.databinding.LocationListBinding
import com.example.hitenpractical.model.LocationEntity

class LocationListAdapter(private val listener: ItemClickListener) :
    RecyclerView.Adapter<LocationListAdapter.ViewHolder>() {


    var mList = ArrayList<LocationEntity>()
    private lateinit var binding: LocationListBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LocationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        holder.bind(item)
        binding.imgDelete.setOnClickListener {

            listener.onDeleteClick(position, item)
        }
        binding.imgEdit.setOnClickListener {
            listener.onEditClick(position, item)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    fun setList(listItems: ArrayList<LocationEntity>) {
        mList = listItems
        notifyItemRangeInserted(0, mList.size)
    }


    class ViewHolder(private val binding: LocationListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocationEntity) {
            binding.item = item

        }
    }

    interface ItemClickListener {
        fun onDeleteClick(position: Int, locationEntity: LocationEntity)

        fun onEditClick(position: Int, locationEntity: LocationEntity)
    }

}