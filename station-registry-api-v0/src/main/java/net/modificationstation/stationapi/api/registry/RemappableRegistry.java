package net.modificationstation.stationapi.api.registry;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;

public interface RemappableRegistry {
	/**
	 * The mode the remapping process should take.
	 */
	enum RemapMode {
		/**
		 * Any differences (local-&gt;remote, remote-&gt;local) are allowed. This should
		 * be used when a side is authoritative (f.e. loading a world on the server).
		 */
		AUTHORITATIVE,
		/**
		 * Entries missing on the remote side are hidden on the local side, while
		 * entries missing on the local side cause an exception. This should be
		 * used when a side is remote (f.e. connecting to a remote server as a
		 * client).
		 */
		REMOTE,
		/**
		 * No differences in entry sets are allowed.
		 */
		EXACT
	}

	void remap(String name, Reference2IntMap<Identifier> remoteIndexedEntries, RemapMode mode) throws RemapException;

	void unmap(String name) throws RemapException;
}
