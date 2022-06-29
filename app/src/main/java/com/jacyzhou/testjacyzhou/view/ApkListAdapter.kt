package com.jacyzhou.testjacyzhou.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jacyzhou.testjacyzhou.R
import com.jacyzhou.testjacyzhou.model.ApkInfo
import com.jacyzhou.testjacyzhou.service.DownloadStatus
import com.jacyzhou.testjacyzhou.service.DownloadStatus.Companion.STATUS_INIT
import com.jacyzhou.testjacyzhou.viewmodel.DownloadApkViewModel

class ApkListAdapter(var viewModel: DownloadApkViewModel) : RecyclerView.Adapter<ApkItemViewHolder>() {
    companion object {
        const val TAG = "ApkListAdapter"
    }

    var data: List<ApkInfo> = arrayListOf()
    var map: MutableMap<Int, DownloadStatus> = ArrayMap()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApkItemViewHolder {
        val apkItem = LayoutInflater.from(parent.context).inflate(R.layout.apk_item_layout, parent, false)
        return ApkItemViewHolder(apkItem)
    }

    override fun onBindViewHolder(holder: ApkItemViewHolder, position: Int) {
        data.getOrNull(position)?.let { apkInfo ->
            // 这个网址翻墙才能访问
            Glide.with(holder.iconView.context).load(apkInfo.icon).into(holder.iconView)
            holder.title.text = apkInfo.title
            val status = map.get(position) ?: DownloadStatus(
                apkPackageName = apkInfo.package_name ?: "",
                adapterCount = position
            )
            holder.action.text = status.getActionText()
            // TODO progressbar 进度更新
            // TODO 过滤重复点击
            holder.action.setOnClickListener { view ->
                if (status.status == STATUS_INIT) {
                    viewModel.downloadApk(apkInfo.download_link, apkInfo.package_name, position)
                }
                // TODO 其他状态处理
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}