package android.os


inline fun <reified T> Bundle.getParcel(key: String): T? {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    getParcelable(key, T::class.java)
  } else {
    @Suppress("DEPRECATION")
    getParcelable(key)
  }
}
