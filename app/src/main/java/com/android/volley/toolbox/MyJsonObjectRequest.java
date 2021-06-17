/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.toolbox;

/**
 *  适用于后台服务端不支持json格式请求
 */
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * A request for retrieving a {@link org.json.JSONObject} response body at a
 * given URL, allowing for an optional {@link org.json.JSONObject} to be passed
 * in as part of the request body.
 */
public class MyJsonObjectRequest extends Request<JSONObject> {
	private Listener<JSONObject> mListener;
	private Map<String, String> postParams;// post 请求参数

	public MyJsonObjectRequest(int method, String url,
			Listener<JSONObject> mlistener, ErrorListener listener) {
		super(method, url, listener);
		mListener = mlistener;
		// TODO Auto-generated constructor stub
	}

	public MyJsonObjectRequest(String url, Listener<JSONObject> mlistener,
			ErrorListener listener) {
		this(Method.GET, url, mlistener, listener);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));

			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		// TODO Auto-generated method stub
		mListener.onResponse(response);
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		return postParams;
	}

}
