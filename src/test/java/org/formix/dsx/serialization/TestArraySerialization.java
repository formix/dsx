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
package org.formix.dsx.serialization;

import org.formix.dsx.XmlElement;
import org.formix.dsx.serialization.XmlSerializer;
import org.formix.dsx.serialization.entities2.ArrayContainer;
import org.junit.Assert;
import org.junit.Test;

public class TestArraySerialization {

	@Test
	public void testArraySerialization() throws Exception {
		XmlSerializer ser = new XmlSerializer();
		XmlElement elem = ser.serialize(new String[] { "Hel\"lo", "world!" },
				"strings");
		System.out.println(elem.toString());
	}

	@Test
	public void testSerializationOfObjectContainingStringArray()
			throws Exception {
		ArrayContainer ac = new ArrayContainer();
		ac.setStrings(new String[] { "Hello", "world!" });
		XmlSerializer ser = new XmlSerializer();
		XmlElement elem = ser.serialize(ac);
		System.out.println(elem);
		Assert.assertEquals("<arrayContainer>"
				+ "<strings><string>Hello</string>"
				+ "<string>world!</string></strings></arrayContainer>",
				elem.toString());
	}

	@Test
	public void testSerializationOfObjectContainingIntegerArray()
			throws Exception {
		ArrayContainer ac = new ArrayContainer();
		ac.setIntegers(new Integer[] { 1, 2 });
		XmlSerializer ser = new XmlSerializer();
		XmlElement elem = ser.serialize(ac);
		System.out.println(elem);
		Assert.assertEquals("<arrayContainer>"
				+ "<integers><integer>1</integer>"
				+ "<integer>2</integer></integers></arrayContainer>",
				elem.toString());
	}
	
	
}
