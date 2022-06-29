package com.jacyzhou.testjacyzhou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jacyzhou.testjacyzhou.view.ApkListAdapter
import com.jacyzhou.testjacyzhou.viewmodel.DownloadApkViewModel

class MainActivity : AppCompatActivity() {
    private val activityModel by lazy {
        ViewModelProvider(this).get(DownloadApkViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val apkListRecyclerView = findViewById<RecyclerView>(R.id.apk_list_recycler)
        apkListRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = ApkListAdapter(activityModel)
        apkListRecyclerView.adapter = adapter
        activityModel.apkListLiveData.observe(this) {
            adapter.data = it
            adapter.notifyDataSetChanged()
        }
        activityModel.downApkStatusData.observe(this) {
            if (it.adapterCount >= 0 && it.adapterCount < adapter.data.size) {
                adapter.map[it.adapterCount] = it
                adapter.notifyItemChanged(it.adapterCount)
            }
        }
        activityModel.getApkList()
    }
}