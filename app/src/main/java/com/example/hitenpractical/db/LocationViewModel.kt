package com.example.hitenpractical.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hitenpractical.model.LocationEntity
import kotlinx.coroutines.launch

class LocationViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val dao = LocationDatabase.getDatabase(application)?.locationDao()
    private val repository = LocationRepository(dao!!)
    val getAllLocation : LiveData<List<LocationEntity>> = repository.getAllLocation()
    init {
        getAllLocation()
    }
    fun insert(locationEntity: LocationEntity) = viewModelScope.launch {
        repository.insert(locationEntity)
    }

    fun update(locationEntity: LocationEntity) = viewModelScope.launch {
        repository.update(locationEntity)
    }

    fun delete(locationEntity: LocationEntity) = viewModelScope.launch {
        repository.delete(locationEntity)
    }

     fun getAllLocation(): LiveData<List<LocationEntity>> {
        return repository.getAllLocation()

    }
}