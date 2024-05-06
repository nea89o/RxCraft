package moe.nea.rxcraft.impl

internal class Cache<T> private constructor(
	var value: Any?
) {
	inline fun getCachedOrCompute(block: () -> T): T {
		if (value == CACHED_VALUE_INVALID_SENTINEL)
			value = block()
		@Suppress("UNCHECKED_CAST")
		return value as T
	}

	fun invalidate() {
		value = CACHED_VALUE_INVALID_SENTINEL
	}

	companion object {
		fun <T> empty(): Cache<T> {
			return Cache(CACHED_VALUE_INVALID_SENTINEL)
		}
		internal val CACHED_VALUE_INVALID_SENTINEL = Object()

	}
}