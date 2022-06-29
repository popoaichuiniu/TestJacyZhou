package com.jacyzhou.testjacyzhou.view

import android.view.View
import android.view.ViewStub
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jacyzhou.testjacyzhou.R

class ApkItemViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
    val iconView: ImageView = itemView.findViewById(R.id.icon)
    var action: Button = itemView.findViewById(R.id.action_button)
    var title: TextView = itemView.findViewById(R.id.title)
    var status_view: ViewStub = itemView.findViewById(R.id.status_view)
}