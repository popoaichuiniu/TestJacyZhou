package com.jacyzhou.testjacyzhou.service

class DownloadStatus(var status: Int = STATUS_INIT, var apkPackageName: String = "", var adapterCount: Int = -1) {
    companion object {
        const val STATUS_INIT = -1
        const val STATUS_DOWNLOADING = 1
        const val STATUS_FAILED = 2
        const val STATUS_COMPLETED = 3
    }

    var progress = -1

    fun getActionText(): String {
        if (status == STATUS_INIT) {
            return "DOWNLOAD";
        }
        if (status == STATUS_DOWNLOADING) {
            return "CANCEL"
        }
        if (status == STATUS_FAILED) {
            return "RETRY"
        }
        if (status == STATUS_COMPLETED) {
            return "INSTALL"
        }
        return ""
    }
}
