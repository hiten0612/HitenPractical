package com.example.hitenpractical.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hitenpractical.WrapContentLinearLayoutManager
import com.example.hitenpractical.adapters.LocationListAdapter
import com.example.hitenpractical.databinding.LocationSourceBinding
import com.example.hitenpractical.db.LocationViewModel
import com.example.hitenpractical.model.LocationEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class LocationSourceActivity : AppCompatActivity(), LocationListAdapter.ItemClickListener {
    private lateinit var binding: LocationSourceBinding
    private var locationListAdapter: LocationListAdapter? = null
    private var locationViewModel: LocationViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LocationSourceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]


        locationListAdapter = LocationListAdapter(this)
        binding.rvLocation.layoutManager = WrapContentLinearLayoutManager(this)
        binding.rvLocation.adapter = locationListAdapter




        locationViewModel?.getAllLocation?.observeForever {
            it?.let {
                locationListAdapter?.setList(it as ArrayList<LocationEntity>)
            }
        }

        setOnClick()


    }

    private fun setOnClick() {
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this@LocationSourceActivity, MainActivity::class.java))
        }


        binding.imgOrder.setOnClickListener {
            val builder = AlertDialog.Builder(this@LocationSourceActivity)

            builder.setTitle("Sort")
            builder.setPositiveButton("Ascending") { dialogInterface, which ->
                locationListAdapter?.let {
                    it.mList.sortBy { it.name }
                    it.notifyItemRangeChanged(0, it.itemCount)
                }
            }

            builder.setNegativeButton("Descending") { dialogInterface, which ->
                locationListAdapter?.let {
                    it.mList.sortByDescending { it.name }
                    it.notifyItemRangeChanged(0, it.itemCount)
                }
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    override fun onDeleteClick(position: Int, locationEntity: LocationEntity) {
        locationViewModel?.delete(locationEntity)
        locationListAdapter?.mList?.remove(locationEntity)
        runBlocking {
            delay(500)
            runOnUiThread {
                locationListAdapter?.notifyItemRemoved(position)
            }
        }

    }

    override fun onEditClick(position: Int, locationEntity: LocationEntity) {

        val intent = Intent(this@LocationSourceActivity, MainActivity::class.java)
        intent.putExtra("item", locationEntity)
        startActivity(intent)


    }
}