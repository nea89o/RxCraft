package moe.nea.rxcraft.impl

import moe.nea.rxcraft.Observer
import moe.nea.rxcraft.Provider
import moe.nea.rxcraft.Subscription

class MappedProvider<T, V>(
	val upstream: Provider<V>,
	val mapper: (V) -> T,
) : Provider<T>, Observer<V> {
	private val subscription = upstream.subscribe(this)
	private val cachedValue: Cache<T> = Cache.empty()
	private val subscriptionModel = BasicSubscriptionModel<T>()

	override fun get(): T {
		return cachedValue.getCachedOrCompute { mapper(upstream.get()) }
	}

	override fun onScopeExit() {
		subscription.drop()
	}

	override fun subscribe(observer: Observer<T>): Subscription {
		return subscriptionModel.putSubscription(observer)
	}

	override fun <V> map(mapper: (T) -> V): Provider<V> {
		return MappedProvider(this, mapper)
	}

	override fun onUpdate() {
		cachedValue.invalidate()
		subscriptionModel.getSubscribers().forEach {
			it.onUpdate()
		}
	}
}