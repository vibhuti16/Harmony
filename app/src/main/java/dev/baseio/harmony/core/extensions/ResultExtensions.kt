package dev.baseio.harmony.core.extensions

import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.data.DaoResult
import retrofit2.Response

fun Response<*>?.isSuccessAndNotNull():Boolean = this?.let {
    it.body() != null && it.isSuccessful
} ?: run {
    false
}

fun ApiResult<*>?.isSuccessAndNotNull() =
    this is ApiResult.Success && this.data != null


fun DaoResult?.isSuccessAndNotNull() = this?.let {
    it.data != null && it.isSucces
} ?: run {
   false
}

fun ApiResult<*>?.getResult(): Any? =  when(this) {
    is ApiResult.Success -> this.data
    else -> null
}
