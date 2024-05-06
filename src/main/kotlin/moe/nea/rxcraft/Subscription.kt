package moe.nea.rxcraft

interface Subscription : ScopeMember {
	fun drop() {
		onScopeExit()
	}
}