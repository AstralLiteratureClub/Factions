package bet.astral.eventor;

public @interface Listener {
	boolean ignoreCancelled();
	Cast listening() default Cast.ASYNC_SYNC;
	Priority priority() default Priority.NORMAL;

	enum Cast {
		ASYNC_SYNC,
		ASYNCHRONOUS,
		SYNCHRONOUS
	}
	enum Priority {
		/**
		 * Called first
		 */
		LOWEST,
		NORMAL,
		/**
		 * Called second last
		 */
		HIGH,
		/**
		 * Called lastly, but modifications to the event on this stage are no longer used.
		 */
		MONITOR
	}
}
