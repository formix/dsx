/****************************************************************************
 * Copyright 2009-2014 Jean-Philippe Gravel, P. Eng. CSDP
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package org.formix.dsx.serialization.entities;

import java.util.Arrays;

/**
 * Utility class.
 * 
 * @author Jean-Philippe Gravel, eng.
 */
final class Util {

	/**
	 * This utility class cannot be instanciated.
	 */
	private Util() {}
	
	public static int hashCode(byte[] obj) {
		if (obj == null)
			return 0;
		return Arrays.hashCode(obj);
	}
	
	public static int hashCode(Object obj) {
		if (obj == null)
			return 0;
		return obj.hashCode();
	}
	
	public static boolean equals(byte[] a, byte[] b) {
		return Arrays.equals(a, b);
	}
		
	public static boolean equals(Object a, Object b) {
		if ((a == null) && (b == null))
			return true;
		if (a != null)
			return a.equals(b);
		return false;
	}
	
}
