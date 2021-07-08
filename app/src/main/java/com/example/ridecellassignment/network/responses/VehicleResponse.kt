package com.example.ridecellassignment.network.responses

import com.google.gson.annotations.SerializedName
data class VehicleData(

	@field:SerializedName("vehicle_make")
	val vehicleMake: String? = null,

	@field:SerializedName("remaining_mileage")
	val remainingMileage: Double? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("lng")
	val lng: Double? = null,

	@field:SerializedName("vehicle_pic_absolute_url")
	val vehiclePicAbsoluteUrl: String? = null,

	@field:SerializedName("transmission_mode")
	val transmissionMode: String? = null,

	@field:SerializedName("vehicle_type")
	val vehicleType: String? = null,

	@field:SerializedName("is_available")
	val isAvailable: Boolean? = null,

	@field:SerializedName("license_plate_number")
	val licensePlateNumber: String? = null,

	@field:SerializedName("vehicle_type_id")
	val vehicleTypeId: Double? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("remaining_range_in_meters")
	val remainingRangeInMeters: Double? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
)
