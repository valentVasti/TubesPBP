package server.api

class QueueApi {
    companion object{
        val BASE_URL = "http://192.168.24.85:8081/CI4_SERVERTUBES/public/"

        val GET_ALL_URL = BASE_URL + "queue/"
        val GET_BY_ID_URL = BASE_URL + "queue/"
        val ADD_URL = BASE_URL + "queue"
        val UPDATE_URL = BASE_URL + "queue/"
        val DELETE_URL = BASE_URL + "queue/"
    }
}