package com.infendro.tasks2do

import java.io.Serializable

class LocationInfo(var address: String, var longitude: Double, var latitude: Double) : Serializable {
    fun toString(longitude: String, latitude: String): String {
        return "$address\n$longitude: ${this.longitude}\n$latitude: ${this.latitude}"
    }
}