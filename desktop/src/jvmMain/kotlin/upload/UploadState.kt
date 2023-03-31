package upload

sealed class UploadState<T> private constructor() {
    class Empty<T> : UploadState<T>()
    data class Active<T>(val targetList: MutableList<T>) : UploadState<T>()
}