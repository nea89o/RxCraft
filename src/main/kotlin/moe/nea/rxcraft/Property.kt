package moe.nea.rxcraft

import moe.nea.rxcraft.impl.FloatingProperty
import java.util.function.Supplier

interface Property<T> : Provider<T> {
	fun <V> map(mapper: (T) -> V, comapper: (V) -> T): Property<V>
	fun set(value: T)

	companion object {
		@JvmStatic
		fun <T> floating(initialValue: T): FloatingProperty<T> {
			return FloatingProperty.withValue(initialValue)
		}

		@JvmStatic
		fun <T> floating(provider: Supplier<T>): FloatingProperty<T> {
			return FloatingProperty.withProvider(provider)
		}
	}
}