package server.api

class UserApi {
    companion object{
        val BASE_URL = "http://192.168.18.246:8081/CI4_SERVERTUBES/public/"

        val GET_ALL_URL = BASE_URL + "user/"
        val GET_BY_ID_URL = BASE_URL + "user/"
        val ADD_URL = BASE_URL + "user"
        val UPDATE_URL = BASE_URL + "user/"
        val DELETE_URL = BASE_URL + "user/"
    }
}