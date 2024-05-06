package moe.nea.rxcraft.impl

import moe.nea.rxcraft.Observer
import moe.nea.rxcraft.Subscription

class BasicSubscriptionModel<T> {
	private val subscriptions = mutableMapOf<Int, Observer<T>>()
	private var nextSubscriptionId = 0
	fun putSubscription(observer: Observer<T>): Subscription {
		return object : Subscription {
			val subscriptionId = nextSubscriptionId++
			override fun onScopeExit() {
				subscriptions.remove(subscriptionId)
			}
		}
	}

	fun getSubscribers(): Collection<Observer<T>> {
		return subscriptions.values
	}
}