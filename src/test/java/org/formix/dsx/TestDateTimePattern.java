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
package org.formix.dsx;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestDateTimePattern {

	private final String DATETIME_PATTERN = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})([+-]\\d{2}:\\d{2})";
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");

	@Test
	public void testDateTimePattern() throws Exception {
		Pattern p = Pattern.compile(DATETIME_PATTERN);
		Matcher m = p.matcher("2012-02-22T17:08:46-04:00");
		if (m.find()) {
			for (int i = 1; i <= m.groupCount(); i++) {
				System.out.println(m.group(i));
			}
		}

		Date d = DATE_FORMAT
				.parse("2012-02-22T17:08:46-04:00"
						.replaceFirst(
								"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})([+-]\\d{2}:\\d{2})",
								"$1GMT$2"));
		
		System.out.println(DATE_FORMAT.format(d));
	}

}
