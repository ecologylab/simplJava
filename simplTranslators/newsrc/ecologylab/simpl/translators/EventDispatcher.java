package ecologylab.simpl.translators;

import java.util.Collection;

/**
 * An event dispatcher.
 * 
 * @author quyin
 * @param <EventHandler>
 */
public interface EventDispatcher<EventHandler>
{

	/**
	 * Add a new eventHandler.
	 * 
	 * @param eventHandler
	 */
	void addEventHandler(EventHandler eventHandler);

	/**
	 * Remove an existing eventHandler.
	 * 
	 * @param eventHandler
	 */
	void removeEventHandler(EventHandler eventHandler);

	/**
	 * @return All existing eventHandlers.
	 */
	Collection<EventHandler> getEventHandlers();

	/**
	 * Dispatch an event (with given args) to all eventHandlers.
	 * 
	 * The number and types of args should be consistent with the definition of EventHandler.
	 * 
	 * @param args
	 */
	void dispatchEvent(Object... args);

}
