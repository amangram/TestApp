package kg.amangram.testapp.data.location

class LocationDTO {
    var latitude = 0.0
    var longitude = 0.0
    override fun toString(): String {
        return "lat:$latitude- lng:$longitude"
    }
}