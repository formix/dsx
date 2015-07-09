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
package org.formix.dsx.serialization.entities2;

public class ArrayContainer {

	private String[] strings;
	private Integer[] integers;
	private Long[] longs;
	private Double[] doubles;
	private Float[] floats;

	public ArrayContainer() {
		this.strings = null;
		this.integers = null;
		this.longs = null;
		this.doubles = null;
		this.floats = null;
	}

	public String[] getStrings() {
		return strings;
	}

	public void setStrings(String[] strings) {
		this.strings = strings;
	}

	public Integer[] getIntegers() {
		return integers;
	}

	public void setIntegers(Integer[] integers) {
		this.integers = integers;
	}

	public Long[] getLongs() {
		return longs;
	}

	public void setLongs(Long[] longs) {
		this.longs = longs;
	}

	public Double[] getDoubles() {
		return doubles;
	}

	public void setDoubles(Double[] doubles) {
		this.doubles = doubles;
	}

	public Float[] getFloats() {
		return floats;
	}

	public void setFloats(Float[] floats) {
		this.floats = floats;
	}

}
