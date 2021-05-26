package aaron

import aaron.{Kilometres, LatLng}

object HaversineDistance {

  def apply(from: LatLng, to: LatLng): Kilometres = {
    val LatLng(lat1, lon1) = from
    val LatLng(lat2, lon2) = to
    val R = 6371 // Radius of the earth in km
    val dLat = deg2rad(lat2 - lat1) // deg2rad below
    val dLon = deg2rad(lon2 - lon1)
    val a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
          Math.sin(dLon / 2) * Math.sin(dLon / 2)

    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    Kilometres(R * c)
  }

  def deg2rad(deg: Double) = deg * (Math.PI / 180)

}
