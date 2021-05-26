package aaron

import aaron.Kilometres

case class LatLng(lat: Double, long: Double) {


  //https://maps.googleapis.com/maps/api/distancematrix/outputFormat?parameters
  def distanceTo(to: LatLng): Kilometres =  HaversineDistance(this, to)

}
