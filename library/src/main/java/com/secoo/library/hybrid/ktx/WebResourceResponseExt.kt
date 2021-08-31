package com.secoo.library.hybrid.ktx

import android.webkit.WebResourceResponse

fun WebResourceResponse.toDescription(): String {
    return "MimeType=$mimeType;encoding=$encoding;statusCode=$statusCode;reasonPhrase=$reasonPhrase;" +
            "responseHeaders=$responseHeaders"
}