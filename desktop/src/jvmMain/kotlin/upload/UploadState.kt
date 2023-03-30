package upload

sealed class UploadState<T> private constructor() {
    data class Active<T>(val targetList: MutableList<T>) : UploadState<T>()
    class Empty<T> : UploadState<T>()
}