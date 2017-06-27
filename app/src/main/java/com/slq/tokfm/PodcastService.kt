package com.slq.tokfm

import android.content.Context
import android.os.Environment
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.jsoup.Jsoup
import java.io.File
import java.lang.String.format
import java.math.BigInteger
import java.security.MessageDigest


object PodcastService {
    fun listPodcasts(): List<Podcast> {
        val pageNum = 0
        val url = "http://audycje.tokfm.pl/podcasts?offset=$pageNum"
        Log.d("PodcastService", "URL: $url")
        val json = Jsoup.connect(url).ignoreContentType(true).execute().body()
        Log.d("PodcastService", "Json: $json")
        val gson = Gson()
        val container = gson.fromJson<PodcastContainer>(json, PodcastContainer::class.java)
        Log.d("PodcastService", "Records: $container")
        Log.d("PodcastService", "Podcasts: ${container.records}")
        return container.records
    }

    fun downlaod(context: Context, podcast: Podcast) {
        Log.d("PodcastService", "Downloading started $podcast")

        val file = File(Environment.DIRECTORY_MUSIC, "${podcast.podcast_id}.mp3")


        val hexTime = currentTimeSecondsToHex()
        val audioName = podcast.podcast_audio
        val digest = digest(hexTime, audioName)
        val bigInt = BigInteger(1, digest)
        var mdp = bigInt.toString(16).toLowerCase()
        mdp = mdp.padStart(32, '0')
        val uri = format("http://storage.tuba.fm/podcast/%s/%s/%s", mdp, hexTime, audioName)

        // Instantiate the RequestQueue.
//        val queue = Volley.newRequestQueue(context)


        // Request a string response from the provided URL.
//        val stringRequest = StringRequest(Request.Method.GET, uri,
//                Response.Listener<String> { response ->
//                     Display the first 500 characters of the response string.
//                    Log.d("Podcast Service", "Response is: " + response.substring(0, 500))
//                }, Response.ErrorListener { Log.d("Podcast Service", "That didn't work!") })
//         Add the request to the RequestQueue.
//        queue.add(stringRequest)


        val request = InputStreamVolleyRequest(Request.Method.GET, uri,
                object : Response.Listener<ByteArray> {
                    override fun onResponse(response: ByteArray?) {
                        try {
                            if (response != null) {
                                val outputStream = context.openFileOutput("olo.mp3", Context.MODE_PRIVATE)
                                outputStream.write(response);
                                outputStream.close();
                                Log.d("Podcast Service", "Download complete.")
                            }
                        } catch (e: Exception) {
                            // TODO Auto-generated catch block
                            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                            e.printStackTrace();
                        }
                    }
                }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                error?.printStackTrace();
            }
        }, null);

        val queue = Volley.newRequestQueue(context, HurlStack());
        queue.add(request);




        Log.d("PodcastService", "Downloading finished $podcast")
    }

    private fun currentTimeSecondsToHex(): String {
        return format("%x", Math.floor(System.currentTimeMillis() / 1000.0).toInt()).toUpperCase()
    }

    private fun digest(n: String, audioName: String): ByteArray {
        val MD5_PHRASE = "MwbJdy3jUC2xChua/"
        val message = MD5_PHRASE + audioName + n
        val md5 = MessageDigest.getInstance("MD5")
        val bytes = message.toByteArray(charset("UTF-8"))
        return md5.digest(bytes)
    }


    class InputStreamVolleyRequest(method: Int,
                                   mUrl: String,
                                   val mListener: Response.Listener<ByteArray>,
                                   errorListener: Response.ErrorListener,
                                   params: HashMap<String, String>?) : Request<ByteArray>(method, mUrl, errorListener) {

        val mParams: Map<String, String>?

        //create a static map for directly accessing headers
        var responseHeaders: Map<String, String>? = null

        init {
            // this request would never use cache.
            setShouldCache(false)
            mParams = params
        }

        override fun getParams(): Map<String, String>? = mParams


        override fun deliverResponse(response: ByteArray) = mListener.onResponse(response)

        override fun parseNetworkResponse(response: NetworkResponse): Response<ByteArray> {
            responseHeaders = response.headers

            //Pass the response data here
            return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response))
        }
    }

}
