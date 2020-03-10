import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import com.google.gson.Gson
import dog.snow.androidrecruittest.MainActivity
import dog.snow.androidrecruittest.repository.model.Global
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.ui.model.Detail
import dog.snow.androidrecruittest.ui.model.ListItem
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class DownloadDataService() : AsyncTask<String, Int, Long>() {

    private val url = "https://jsonplaceholder.typicode.com/"
    private var userList = HashMap<Int,RawUser>()
    private var photoList = HashMap<Int,RawPhoto>()
    private var albumList = HashMap<Int,RawAlbum>()

    private var downloadedAlbums = java.util.ArrayList<Int>()
    private var downloadedUsers = java.util.ArrayList<Int>()

    private val limit = 100


    override fun doInBackground(vararg parts: String): Long? {

                downloadPhotos()
                downloadAlbums()
                downloadUsers()
                storeData()


        return 0
    }


    private fun storeData(){
        Global.getInstance()!!.itemList = createItemList()
        Global.getInstance()!!.detailList = createDetailList()
    }

    private fun createItemList(): ArrayList<ListItem>{
        val result = ArrayList<ListItem>()
        for((_,photo) in photoList){
            val album = albumList[photo.albumId]
            val item = ListItem(
                id = photo.id,
                title = photo.title,
                thumbnailUrl = photo.thumbnailUrl,
                albumTitle = album!!.title
            )
            result.add(item)
        }
        return result
    }

    private fun createDetailList(): HashMap<Int,Detail>{
        val result = HashMap<Int,Detail>()
        for((_,photo) in photoList) {
            val album = albumList[photo.albumId]
            val user = userList[album!!.userId]
            val item = Detail(
                photoId = photo.id,
                photoTitle = photo.title,
                albumTitle = album.title,
                username = user!!.username,
                email = user.email,
                phone = user.phone,
                url = photo.url
            )
            result[item.photoId] = item
        }
        return result
    }

    private fun downloadPhotos(){
        val list = Gson().fromJson(getData("photos","?_limit=$limit"),Array<RawPhoto>::class.java).toList() as ArrayList<RawPhoto>
        for(item : RawPhoto in list){
            photoList[item.id] = item
        }
    }

    private fun downloadUsers(){
        val rawUsers = StringBuilder()
        rawUsers.append("[")
        for((_, album) in albumList){
            val id = album.userId
            if(!downloadedUsers.contains(id)){
                rawUsers.append(getData("users","/$id"))
                rawUsers.append(",")
                downloadedUsers.add(id)
            }
        }
        rawUsers.deleteCharAt(rawUsers.length-1)
        if(downloadedUsers.size > 1){
            rawUsers.append("]")
            val list = Gson().fromJson(rawUsers.toString(),Array<RawUser>::class.java).toList() as ArrayList<RawUser>
            for(item : RawUser in list){
                userList[item.id] = item
            }
        }
        else{
            rawUsers.deleteCharAt(0)
            val list = Gson().fromJson(rawUsers.toString(),RawUser::class.java)
            userList.put(list.id,list)
        }
    }

    private fun downloadAlbums(){
        val rawAlbums = StringBuilder()
        rawAlbums.append("[")
        for((_, photo) in photoList){
            val id = photo.albumId
            if(!downloadedAlbums.contains(id)){
                rawAlbums.append(getData("albums","/$id"))
                rawAlbums.append(",")
                downloadedAlbums.add(id)
            }
        }
        rawAlbums.deleteCharAt(rawAlbums.length-1)
        if(downloadedAlbums.size > 1){
            rawAlbums.append("]")
            val list = Gson().fromJson(rawAlbums.toString(),Array<RawAlbum>::class.java).toList() as ArrayList<RawAlbum>
            for(item : RawAlbum in list){
                albumList[item.id] = item
            }
        }
        else{
            rawAlbums.deleteCharAt(0)
            val list = Gson().fromJson(rawAlbums.toString(),RawAlbum::class.java)
            albumList.put(list.id,list)
        }
    }

    private fun getData(data : String, parameters : String): String {
        val endpoint = URL(url+data+parameters)
        with(endpoint.openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                return response.toString()
            }
        }
    }

    override fun onPostExecute(result: Long?) {
        if(!Global.getInstance()!!.isError){
            Global.getInstance()!!.isDataDownloaded = true
        }
    }
}