package server.api

class BengkelApi {
    companion object{
        val BASE_URL = "http://192.168.1.16:8081/CI4_SERVERTUBES/public/"

        val GET_ALL_URL = BASE_URL + "bengkel/"
        val GET_BY_ID_URL = BASE_URL + "bengkel/"
        val ADD_URL = BASE_URL + "bengkel"
        val UPDATE_URL = BASE_URL + "bengkel/"
        val DELETE_URL = BASE_URL + "bengkel/"
    }
}