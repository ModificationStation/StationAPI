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

package io.github.minecraftcursedlegacy.accessor.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(EntityRegistry.class)
public interface AccessorEntityRegistry {
	@Accessor("ID_TO_CLASS")
	static Map<Integer, Class<? extends Entity>> getIdToClassMap() {
		throw new AssertionError("mixin");
	}

	@Accessor("ID_TO_CLASS")
	static void setIdToClassMap(Map<Integer, Class<? extends Entity>> value) {
		throw new AssertionError("mixin");
	}

	@Accessor("CLASS_TO_ID")
	static Map<Class<? extends Entity>, Integer> getClassToIdMap() {
		throw new AssertionError("mixin");
	}

	@Accessor("CLASS_TO_ID")
	static void setClassToIdMap(Map<Class<? extends Entity>, Integer> value) {
		throw new AssertionError("mixin");
	}

	@Accessor("CLASS_TO_STRING_ID")
	static Map<Class<? extends Entity>, String> getClassToStringIdMap() {
		throw new AssertionError("mixin");
	}

	@Accessor("CLASS_TO_STRING_ID")
	static void setClassToStringIdMap(Map<Class<? extends Entity>, String> value) {
		throw new AssertionError("mixin");
	}

	@Accessor("STRING_ID_TO_CLASS")
	static Map<String, Class<? extends Entity>> getStringIdToClassMap() {
		throw new AssertionError("mixin");
	}

	@Accessor("STRING_ID_TO_CLASS")
	static void setStringIdToClassMap(Map<String, Class<? extends Entity>> value) {
		throw new AssertionError("mixin");
	}

	@Invoker
	static void callRegister(Class<? extends Entity> arg, String string, int i) {
		throw new AssertionError("mixin");
	}
}
