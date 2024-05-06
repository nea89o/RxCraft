package moe.nea.rxcraft.impl

import moe.nea.rxcraft.Observer
import moe.nea.rxcraft.Property
import moe.nea.rxcraft.Provider
import moe.nea.rxcraft.Subscription

class MappedProperty<T, V>(
	val upstream: Property<V>,
	val map: (V) -> T,
	val comap: (T) -> V,
) : Property<T>, Observer<V> {
	private val subscription = upstream.subscribe(this)
	private val cachedValue: Cache<T> = Cache.empty()
	private val subscriptionModel = BasicSubscriptionModel<T>()

	override fun <V> map(mapper: (T) -> V, comapper: (V) -> T): Property<V> {
		return MappedProperty(this, mapper, comapper)
	}

	override fun <V> map(mapper: (T) -> V): Provider<V> {
		return MappedProvider(this, mapper)
	}

	override fun set(value: T) {
		upstream.set(comap(value))
	}

	override fun get(): T {
		return cachedValue.getCachedOrCompute { map(upstream.get()) }
	}

	override fun subscribe(observer: Observer<T>): Subscription {
		return subscriptionModel.putSubscription(observer)
	}

	override fun onScopeExit() {
		subscription.drop()
	}

	override fun onUpdate() {
		cachedValue.invalidate()
		subscriptionModel.getSubscribers().forEach {
			it.onUpdate()
		}
	}

}