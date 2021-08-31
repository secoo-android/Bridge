package com.secoo.library.hybrid.ktx

import android.net.http.SslError
import com.secoo.coobox.library.ktx.kotlin.removeLineBreaks

fun SslError?.toDescription(): String {
    return if (this == null) {
        ""
    } else {
        "url=${this.url};error=${this.primaryError};certificate=${this.certificate?.toString()?.removeLineBreaks()}"
    }
}