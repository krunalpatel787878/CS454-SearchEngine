package edu.calstatela.cs454;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class URLQueue implements Queue {
	
	LinkedList evenQueue;
	LinkedList oddQueue;
	Set gatheredLinks;
	Set processedLinks;

	/**
	 * Maximum number of elements allowed in the gatheredLinks set
	 */
	int maxElements;

	/**
	 * Additional data to be passed to the thread: A filename prefix that
	 * specifies where the spidered files should be stored
	 */
	String filenamePrefix;

	public URLQueue() {
		evenQueue = new LinkedList();
		oddQueue = new LinkedList();
		gatheredLinks = new HashSet();
		processedLinks = new HashSet();
		maxElements = -1;
		filenamePrefix = "";
	}

	public URLQueue(int _maxElements, String _filenamePrefix) {
		evenQueue = new LinkedList();
		oddQueue = new LinkedList();
		gatheredLinks = new HashSet();
		processedLinks = new HashSet();
		maxElements = _maxElements;
		filenamePrefix = _filenamePrefix;
	}

	/**
	 * Setter for filename prefix
	 */
	public void setFilenamePrefix(String _filenamePrefix) {
		filenamePrefix = _filenamePrefix;
	}

	/**
	 * Getter for filename prefix
	 */
	public String getFilenamePrefix() {
		return filenamePrefix;
	}

	/**
	 * Set the maximum number of allowed elements
	 */
	public void setMaxElements(int _maxElements) {
		maxElements = _maxElements;
	}

	/**
	 * Return all links gathered so far
	 * This method is not synchronized, so use it with care when in a
	 * multithreaded envireonment.
	 */
	public Set getGatheredElements() {
		return gatheredLinks;
	}

	/**
	 * Return all links processed so far
	 * This method is not synchronized, so use it with care when in a
	 * multithreaded envireonment.
	 */
	public Set getProcessedElements() {
		return processedLinks;
	}

	/**
	 * Return how many elements are in the queue
	 */
	public int getQueueSize(int level) {
		if (level % 2 == 0) {
			return evenQueue.size();
		} else {
			return oddQueue.size();
		}
	}

	/**
	 * Return how many links have been processed yet
	 */
	public int getProcessedSize() {
		return processedLinks.size();
	}

	/**
	 * Return how many links have been gathered yet
	 */
	public int getGatheredSize() {
		return gatheredLinks.size();
	}

	/**
	 * Return and remove the first element from the appropriate queue
	 * Note that the return type of this method is Object for compliance
	 * with interface Queue.
	 */
	public synchronized Object pop(int level) {
		String s;
		// try to get element from the appropriate queue
		// is the queue is empty, return null
		if (level % 2 == 0) {
			if (evenQueue.size() == 0) {
				return null;
			} else {
				s = (String) evenQueue.removeFirst();
			}
		} else {
			if (oddQueue.size() == 0) {
				return null;
			} else {
				s = (String) oddQueue.removeFirst();
			}
		}
		// convert the string to a url and add to the set of processed links
		try {
			URL url = new URL(s);
			processedLinks.add(s);
			return url;
		} catch (MalformedURLException e) {
			// shouldn't happen, as only URLs can be pushed
			return null;
		}
	}

	/**
	 * Add an element at the end of the appropriate queue
	 * Note that the type of argument url is Object for compliance with
	 * interface Queue.
	 */
	public synchronized boolean push(Object url, int level) {
		// don't allow more than maxElements links to be gathered
		if (maxElements != -1 && maxElements <= gatheredLinks.size())
			return false;
		String s = ((URL) url).toString();
		if (gatheredLinks.add(s)) {
			// has not been in set yet, so add to the appropriate queue
			if (level % 2 == 0) {
				evenQueue.addLast(s);
			} else {
				oddQueue.addLast(s);
			}
			return true;
		} else {
			// this link has already been gathered
			return false;
		}
	}

	/**
	 * Clear both queues
	 * The sets of gathered and processed Elements are not affected.
	 */
	public synchronized void clear() {
		evenQueue.clear();
		oddQueue.clear();
	}


}
