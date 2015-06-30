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

//import java.util.HashSet;
//import java.util.Set;
//
//import org.formix.dsx.IdGen;
//import org.junit.Assert;
//import org.junit.Test;

public class TestIdGen {

	public TestIdGen() {
	}

//	@Test
//	public void testIdGen() throws Exception {
//		final int ID_COUNT = 500000;
//
//		final Set<Long> ids1 = new HashSet<Long>();
//		Thread t1 = new Thread(new Runnable() {
//			public void run() {
//				for (int i = 0; i < ID_COUNT; i++) {
//					ids1.add(IdGen.nextId());
//				}
//			}
//		});
//		t1.start();
//
//		final Set<Long> ids2 = new HashSet<Long>();
//		Thread t2 = new Thread(new Runnable() {
//			public void run() {
//				for (int i = 0; i < ID_COUNT; i++) {
//					ids2.add(IdGen.nextId());
//				}
//			}
//		});
//		t2.start();
//
//		final Set<Long> ids3 = new HashSet<Long>();
//		Thread t3 = new Thread(new Runnable() {
//			public void run() {
//				for (int i = 0; i < ID_COUNT; i++) {
//					ids3.add(IdGen.nextId());
//				}
//			}
//		});
//		t3.start();
//
//		final Set<Long> ids4 = new HashSet<Long>();
//		Thread t4 = new Thread(new Runnable() {
//			public void run() {
//				for (int i = 0; i < ID_COUNT; i++) {
//					ids4.add(IdGen.nextId());
//				}
//			}
//		});
//		t4.start();
//
//		t1.join();
//		t2.join();
//		t3.join();
//		t4.join();
//
//		final Set<Long> ids = new HashSet<Long>();
//		ids.addAll(ids1);
//		ids.addAll(ids2);
//		ids.addAll(ids3);
//		ids.addAll(ids4);
//		
//		Assert.assertEquals("An id collision occured.", ID_COUNT * 4,
//				ids.size());
//	}

}
