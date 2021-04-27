package com.infendro.tasks2do.Connection

import android.app.Activity
import android.content.Context
import android.location.Criteria
import android.location.LocationManager
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.infendro.tasks2do.LocationInfo

class Location {
    companion object{
        suspend fun getLastKnownLocation(context: Context) : LocationInfo? {
            val longAndLat = getLongAndLat(context)

            return if(longAndLat!=null) LocationInfo(getAddress(longAndLat[0],longAndLat[1]),longAndLat[0],longAndLat[1]) else null

            /*return try {
                val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                val criteria = Criteria()
                criteria.accuracy = Criteria.ACCURACY_FINE
                criteria.isCostAllowed = false

                val bestProvider = locationManager.getBestProvider(criteria,false)
                return if(bestProvider!=null){
                    val location = locationManager.getLastKnownLocation(bestProvider)
                    return if(location!=null){
                        LocationInfo(getAddress(location),location.longitude,location.latitude)
                    }else{
                        Log.println(Log.INFO, "", "no last known location")
                        null
                    }
                }else{
                    Log.println(Log.INFO, "", "no available providers")
                    null
                }
            }catch (ex: SecurityException){
                Log.println(Log.INFO, "", "permission to access location not granted")
                null
            }*/
        }

        fun getLongAndLat(context: Context) : Array<Double>? {
            return try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                val criteria = Criteria()
                criteria.accuracy = Criteria.ACCURACY_FINE
                criteria.isCostAllowed = false

                val bestProvider = locationManager.getBestProvider(criteria,false)
                return if(bestProvider!=null){
                    val location = locationManager.getLastKnownLocation(bestProvider)
                    return if(location!=null){
                        arrayOf(location.longitude, location.latitude)
                    }else{
                        Log.println(Log.INFO, "", "no last known location")
                        null
                    }
                }else{
                    Log.println(Log.INFO, "", "no available providers")
                    null
                }
            }catch (ex: SecurityException){
                Log.println(Log.INFO, "", "permission to access location not granted")
                null
            }
        }

        const val locationIQAPIToken = "pk.bcdfc4e93300f628230dbd8e0d308bd7"

        suspend fun getAddress(longitude: Double, latitude: Double) : String {
            val response = Connection.get("https://eu1.locationiq.com/v1/reverse.php?key=$locationIQAPIToken&lat=$latitude&lon=$longitude&format=json")
            val responseJsonObject = JsonParser().parse(response.response.reader()) as JsonObject

            val addressJsonObject = responseJsonObject.getAsJsonObject("address")

            return if(addressJsonObject.has("country")){ addressJsonObject.get("country").asString + ", " }else{ "" } +
                    if(addressJsonObject.has("state")){ addressJsonObject.get("state").asString }else{ "" } + "\n" +
                    if(addressJsonObject.has("postcode")){ addressJsonObject.get("postcode").asString + " " }else{ "" } +
                    if(addressJsonObject.has("village")){ addressJsonObject.get("village").asString }else{ "" } + "\n" +
                    if(addressJsonObject.has("road")){ addressJsonObject.get("road").asString + " " }else{ "" } +
                    if(addressJsonObject.has("house_number")){ addressJsonObject.get("house_number").asString }else{ "" }
        }
    }
}