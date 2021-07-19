package com.digitalsamurai.gorastudiotest


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


object NetworkService {
    private val URL_USERS = "https://jsonplaceholder.typicode.com/users";
    private val URL_ALBUMS_BY_USERID = "https://jsonplaceholder.typicode.com/albums?userId=";
    private val URL_PHOTOS_BY_ALBUMID = "https://jsonplaceholder.typicode.com/photos?albumId=";


    //храним данные нашей сессии
    private var savedPhotos = HashMap<String, Bitmap>()

    fun savePhoto(set : Pair<String, Bitmap>){
        savedPhotos.put(set.first,set.second)
    }
    fun getPhoto(url : String) : Bitmap?{
        with(savedPhotos.get(url)){
            if (this != null){
                return this
            } else{
                return null
            }
        }
    }


    public fun getUsers() : Deferred<List<User>> = GlobalScope.async(Dispatchers.IO)  {
        val c: Deferred<String> = async { executeNetworkQuery(URL_USERS) }
        val users = convertStringToListUsers(c.await())
        return@async users

    }

    fun getAlbumsByUserId(id : Int) : Deferred<List<Album>> = GlobalScope.async<List<Album>>{
        val c : String = async { executeNetworkQuery(URL_ALBUMS_BY_USERID+id.toString()) }.await()
        val albums = convertStringToListAlbums(c)
        return@async albums
    }

    fun getPhotosByAlbumId(id : Int) : Deferred<List<Photo>> = GlobalScope.async(Dispatchers.IO){
        val c : String = async { executeNetworkQuery(URL_PHOTOS_BY_ALBUMID+id.toString()) }.await()
        val photos = convertStringToListPhotos(c)
        return@async photos
    }

    fun getPhotoByUrl(url : String) : Deferred<Bitmap> = GlobalScope.async(Dispatchers.IO){
        val connection = (URL(url).openConnection()) as HttpURLConnection;
        connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ")
        val bitmap = BitmapFactory.decodeStream(connection.inputStream);
        savePhoto(Pair(url,bitmap))
        return@async bitmap
    }

    //выполняет запрос, заданный по url и возвращает строку результата

    private  fun executeNetworkQuery(url : String): String {
        //todo check internet available
        val connection = (URL(url).openConnection()) as HttpURLConnection;
        val result = readResponse(connection)
        return result
    }

    //считывание строки запроса
    private fun readResponse(connection: HttpURLConnection) : String{
        val s = StringBuilder()
        connection.inputStream.bufferedReader()
            .readLines().forEach{line -> s.append(line)}
        return s.toString()
    }

    //конвертирование строки в список фотографий

    private fun convertStringToListPhotos(s : String) : List<Photo>{
        //String -> jsonArray
        val jsonArray = JSONArray(s);

        //jsonArray->List<Photo>
        val outArray = ArrayList<Photo>()
        for(i : Int in 0..jsonArray.length()-1){
            with(JSONObject(jsonArray.get(i).toString())){
                outArray.add(Photo(this.getString("title"),this.getString("url")))
            }
        }
        return outArray
    }

    //конвертирование строки запроса в список пользователей

    private fun convertStringToListUsers(s : String) : List<User>{
        //String -> jsonArray
        val jsonArray = JSONArray(s)

        //jsonArray -> List<User>
        val outArray = ArrayList<User>()
        for (i : Int in 0..jsonArray.length()-1){
            with(JSONObject(jsonArray.get(i).toString())){
                outArray.add(User(this.getInt("id"), this.getString("name")))
            }
        }
        return outArray
    }

    //конвертирование строки запроса в список альбомов

    private fun convertStringToListAlbums(s : String) : List<Album>{
        //String -> jsonArray
        val jsonArray = JSONArray(s)

        //jsonArray -> List<Album>
        val outArray = ArrayList<Album>()
        for(i : Int in 0..jsonArray.length()-1){
            with(JSONObject(jsonArray.get(i).toString())){
                outArray.add(Album(this.getInt("id")))
            }
        }
        return outArray
    }
//    fun isConnected(): Boolean {
//        val manager = MainApplication.getContext()
//            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        return manager.activeNetworkInfo != null && manager.activeNetworkInfo!!.isConnected
//    }

}

