package io.disquark.rest.kotlin.request

import io.disquark.rest.request.FileUpload

interface FileUploadDsl {
    var files: MutableList<FileUpload>

    operator fun FileUpload.unaryPlus() {
        files + this
    }

    suspend fun fileUpload(block: suspend () -> FileUpload) {
        +block()
    }
}