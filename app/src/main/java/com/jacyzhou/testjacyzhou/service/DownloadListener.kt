package com.jacyzhou.testjacyzhou.service

internal interface DownloadListener {
    fun onProgressUpdate(progress: Int)
    fun onSuccess(file: String?)
    fun onFailure(reason: String?)
}