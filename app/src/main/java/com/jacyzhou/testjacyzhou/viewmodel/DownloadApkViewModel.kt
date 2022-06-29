package com.jacyzhou.testjacyzhou.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jacyzhou.testjacyzhou.model.ApkInfo
import com.jacyzhou.testjacyzhou.service.DownloadListener
import com.jacyzhou.testjacyzhou.service.DownloadStatus
import com.jacyzhou.testjacyzhou.service.IDownloadService
import com.jacyzhou.testjacyzhou.service.TaskManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class DownloadApkViewModel : ViewModel() {
    private val client = OkHttpClient()
    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, e ->
            Log.e(TAG, "Coroutine throw exception", e)
        }
    }
    private val ioScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)
    }

    private val downloadService: IDownloadService by lazy {
        IDownloadService()
    }

    val apkListLiveData: MutableLiveData<List<ApkInfo>> = MutableLiveData()

    val downApkStatusData: MutableLiveData<DownloadStatus> = MutableLiveData()


    companion object {
        private const val APK_LIST_URL = "https://api.dev.al-array.com/demo/1.0/apps"
        private const val TAG = "DownloadApkViewModel"
    }

    fun getApkList() {
        ioScope.launch {
            val apkList: ArrayList<ApkInfo>
            val request = Request.Builder().url(APK_LIST_URL)
                .build()
            try {
                val response = client.newCall(request).execute()
                val result = response.body?.string()
                Log.i(TAG, "$result")
                val type = object : TypeToken<ArrayList<ApkInfo>>() {}.type
                apkList = Gson().fromJson(result, type)
                apkListLiveData.postValue(apkList)
            } catch (e: Exception) {
                Log.i(TAG, "$e")
            }
        }
    }

    inner class DownloadApk(private val url: String, val packageName: String, val position: Int): Runnable {
        override fun run() {
            downloadService.download(url, object : DownloadListener {
                override fun onProgressUpdate(progress: Int) {
                    downApkStatusData.postValue(DownloadStatus().apply {
                        this.progress = progress
                        this.status = DownloadStatus.STATUS_DOWNLOADING
                        this.apkPackageName = packageName
                        this.adapterCount = position
                    })
                }

                override fun onSuccess(file: String?) {
                    downApkStatusData.postValue(DownloadStatus().apply {
                        this.progress = 100
                        this.status = DownloadStatus.STATUS_COMPLETED
                        this.apkPackageName = packageName
                        this.adapterCount = position
                    })
                }

                override fun onFailure(reason: String?) {
                    downApkStatusData.postValue(DownloadStatus().apply {
                        this.progress = 0
                        this.status = DownloadStatus.STATUS_FAILED
                        this.apkPackageName = packageName
                        this.adapterCount = position
                    })
                }
            })
        }

    }

    fun downloadApk(url: String?, packageName: String?, position: Int) {
        if (url == null || packageName == null) {
            return
        }
        val downLoadTask = DownloadApk(url, packageName, position)
        val result = TaskManager.addTask(downLoadTask)
        if (!result) {
            // Toast
            return
        }
        TaskManager.start()
    }
}