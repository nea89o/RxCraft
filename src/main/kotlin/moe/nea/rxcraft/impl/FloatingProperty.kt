package moe.nea.rxcraft.impl

import moe.nea.rxcraft.Observer
import moe.nea.rxcraft.Property
import moe.nea.rxcraft.Provider
import moe.nea.rxcraft.Subscription
import java.util.function.Supplier

class FloatingProperty<T>
internal constructor(var value: Any?, var isLazyInit: Boolean) : Property<T> {
	companion object {
		fun <T> withValue(value: T) = FloatingProperty<T>(value, false)
		fun <T> withProvider(provider: Supplier<T>) = FloatingProperty<T>(provider, true)
	}

	val subscriptionModel = BasicSubscriptionModel<T>()

	override fun <V> map(mapper: (T) -> V, comapper: (V) -> T): Property<V> {
		return MappedProperty(this, mapper, comapper)
	}

	override fun <V> map(mapper: (T) -> V): Provider<V> {
		return MappedProvider(this, mapper)
	}

	override fun set(value: T) {
		this.value = value
		this.isLazyInit = false
		subscriptionModel.getSubscribers().forEach {
			it.onUpdate()
		}
	}

	override fun get(): T {
		if (this.isLazyInit) {
			this.value = (value as Supplier<T>).get()
			this.isLazyInit = false
		}
		return this.value as T
	}

	override fun onScopeExit() {
		// Nothing to do: we do not subscribe to anything
	}

	override fun subscribe(observer: Observer<T>): Subscription {
		return subscriptionModel.putSubscription(observer)
	}
}