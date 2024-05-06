package moe.nea.rxcraft

interface ScopeMember {
	fun onScopeExit()
	fun bindWith(scope: RxScope) {
		scope.bind(this)
	}
}