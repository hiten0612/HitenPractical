package com.example.hitenpractical.db

import androidx.lifecycle.LiveData
import com.example.hitenpractical.model.LocationEntity

class LocationRepository(private val locationDao: LocationDao) {
    private val getAllLocation = locationDao.getAllLocation()

    fun update(locationEntity: LocationEntity) {
        locationDao.update(locationEntity)
    }

    fun insert(locationEntity: LocationEntity) {
        locationDao.insert(locationEntity)
    }

    fun delete(locationEntity: LocationEntity) {
        locationDao.delete(locationEntity)
    }

     fun getAllLocation(): LiveData<List<LocationEntity>> {
        return getAllLocation
    }


}