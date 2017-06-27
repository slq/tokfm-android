package com.slq.tokfm

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser

class ByteRequest(url: String, val responseListener: Response.Listener<ByteArray>)
    : Request<ByteArray>(Request.Method.GET, url, Response.ErrorListener { error -> error?.printStackTrace() }) {

    override fun deliverResponse(response: ByteArray) = responseListener.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse): Response<ByteArray> {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response))
    }
}
