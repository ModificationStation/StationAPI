/*
 * Copyright (c) 2020 The Cursed Legacy Team.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.minecraftcursedlegacy.api.registry;

/**
 * Identifier for game content.
 */
public final class Id {
	public Id(String namespace, String id) {
		this.namespace = namespace;
		this.name = id;
	}

	public Id(String stringForm) {
		String[] strs = stringForm.split(":");

		if (strs.length == 0) {
			throw new RuntimeException("Illegal String Identifier! " + stringForm);
		} else if (strs.length == 1) {
			this.namespace = "minecraft";
			this.name = strs[0].trim();
		} else {
			this.namespace = strs[0].trim();
			this.name = strs[1].trim();
		}
	}

	private final String namespace;
	private final String name;

	public String getNamespace() {
		return this.namespace;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.namespace + ":" + this.name;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof String) {
			return other.equals(this.toString());
		} else if (other instanceof Id) {
			Id otherId = (Id) other;
			return otherId.name.equals(this.name) && otherId.namespace.equals(this.namespace);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = 5;
		result = 29 * result + this.namespace.hashCode();
		result = 29 * result + this.name.hashCode();
		return result;
	}
}
