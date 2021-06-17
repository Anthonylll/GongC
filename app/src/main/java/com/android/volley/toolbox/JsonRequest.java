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

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import java.util.Map;

/**
 * A request for retrieving a T type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 * 
 * @param <T>
 *            JSON type of response expected
 */
public abstract class JsonRequest<T> extends Request<T> {

	/** Default charset for JSON request. */
	protected static final String PROTOCOL_CHARSET = "utf-8";

	/** Content type for request. */
	private static final String PROTOCOL_CONTENT_TYPE = String.format(
			"application/json; charset=%s", PROTOCOL_CHARSET);

	private final Listener<T> mListener;
	private final Map<String, String> mRequestBody;

	/**
	 * Deprecated constructor for a JsonRequest which defaults to GET unless
	 * {@link #getPostBody()} or {@link #getPostParams()} is overridden (which
	 * defaults to POST).
	 * 
	 * @deprecated Use .
	 */
	public JsonRequest(String url, Map<String, String> requestBody,
			Listener<T> listener, ErrorListener errorListener) {
		this(Method.DEPRECATED_GET_OR_POST, url, requestBody, listener,
				errorListener);
	}

	/**
	 * post参数为Map<String,String>
	 * 
	 * @param method
	 *            get 或者 post
	 * @param url
	 *            url地址
	 * @param requestBody
	 *            Map<String,String> 参数可以为null
	 * @param listener
	 *            返回成功监听
	 * @param errorListener
	 *            返回错误监听
	 */
	public JsonRequest(int method, String url, Map<String, String> requestBody,
			Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
		mRequestBody = requestBody;
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

	/**
	 * @deprecated Use {@link #getBodyContentType()}.
	 */
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mRequestBody;
	}

	/**
	 * 返回url+opt+info作为缓存的唯一标示符
	 */
	@Override
	public String getCacheKey() {
		StringBuilder builder = new StringBuilder();
		builder.append(getUrl());
		if (mRequestBody != null) {
			if (mRequestBody.get("opt") != null) {
				builder.append(mRequestBody.get("opt"));
			}
			if (mRequestBody.get("info") != null) {
				builder.append(mRequestBody.get("info"));
			}
		}
		return builder.toString();
	}
}
