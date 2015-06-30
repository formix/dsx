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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.formix.dsx.XmlElement;
import org.formix.dsx.serialization.XmlSerializer;
import org.formix.dsx.serialization.entities.Box;
import org.formix.dsx.serialization.entities.BoxedItem;
import org.formix.dsx.serialization.entities.DataContainer;
import org.formix.dsx.serialization.entities.ExtendedOwner;
import org.formix.dsx.serialization.entities.Owner;
import org.formix.dsx.serialization.entities.OwnerSiteBundle;
import org.formix.dsx.serialization.entities.Site;
import org.formix.dsx.serialization.entities.WorkOrder;
import org.formix.dsx.serialization.entities2.Message;
import org.junit.Assert;
import org.junit.Test;

public class TestXmlSerializer {

	@Test
	public void testSerializationComplexSubTypes() throws Exception {
		OwnerSiteBundle bundle = createOwnerSiteBundle();
		XmlSerializer xs = new XmlSerializer();
		xs.serialize(bundle);

		// TODO: Assert something here...
	}

	@Test
	public void testDeSerializationComplexSubTypes() throws Exception {
		OwnerSiteBundle bundle = createOwnerSiteBundle();
		XmlSerializer xs = new XmlSerializer();
		XmlElement elem = xs.serialize(bundle);

		xs.getClassResolutionPackages().add(
				"org.formix.dsx.serialization.entities");

		OwnerSiteBundle bundle2 = xs.deserialize(elem, OwnerSiteBundle.class);
		XmlElement elem2 = xs.serialize(bundle2);

		Assert.assertEquals(elem.toString(), elem2.toString());
	}

	@Test
	public void testSerializationCollectionOfSimpleTypes() throws Exception {
		WorkOrder wo = this.createWorkOrder();
		XmlSerializer xs = new XmlSerializer();
		XmlElement elem = xs.serialize(wo);

		XmlElement expected = XmlElement.readXML("<workOrder><assetIds>"
				+ "<long>9</long><long>99</long><long>999</long></assetIds>"
				+ "<deviceId>5</deviceId>"
				+ "<dateSent>2012-02-11T11:09:18-05:00</dateSent></workOrder>");

		Assert.assertTrue(
				"String XML not in the expected format\nexpected: " + expected.toString() + "\nactual: " + elem.toString(),
				expected.deepEquals(elem));
	}

	@Test
	public void testDeserializationCollectionOfSimpleTypes() throws Exception {
		WorkOrder wo = this.createWorkOrder();
		XmlSerializer xs = new XmlSerializer();
		XmlElement expectedElem = xs.serialize(wo);
		WorkOrder wo2 = xs.deserialize(expectedElem, WorkOrder.class);
		XmlElement actualElem = xs.serialize(wo2);
		Assert.assertEquals(expectedElem.toString(), actualElem.toString());
	}

	private OwnerSiteBundle createOwnerSiteBundle() {
		OwnerSiteBundle bundle = new OwnerSiteBundle();

		Owner o1 = new Owner();
		o1.setId(2700L);
		o1.setName("UTB");
		bundle.getOwners().add(o1);

		Owner o2 = new Owner();
		o2.setId(1324L);
		o2.setName("CIV");
		bundle.getOwners().add(o2);

		Site s1 = new Site();
		s1.setId(1L);
		s1.setOwnerId(o1.getId());
		s1.setName("Banq-o-bar");
		bundle.getSites().add(s1);

		Site s2 = new Site();
		s2.setId(2L);
		s2.setOwnerId(o2.getId());
		s2.setName("La plage");
		bundle.getSites().add(s2);

		Site s3 = new Site();
		s3.setId(3L);
		s3.setOwnerId(o1.getId());
		s3.setName("Le Bistrot du Fjord");
		bundle.getSites().add(s3);

		return bundle;
	}

	private WorkOrder createWorkOrder() {
		WorkOrder wo = new WorkOrder();

		wo.setDeviceId(5L);
		// 2012-02-11T11:09:18-0500
		Calendar cal = new GregorianCalendar(2012, Calendar.FEBRUARY, 11, 11,
				9, 18);
		wo.setDateSent(new Timestamp(cal.getTimeInMillis()));
		wo.getAssetIds().add(9L);
		wo.getAssetIds().add(99L);
		wo.getAssetIds().add(999L);

		return wo;
	}

	@Test
	public void testBaseTypeCollectionSerialization() throws Exception {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ids.add(1);
		ids.add(1);
		ids.add(2);
		ids.add(3);
		ids.add(5);
		ids.add(8);

		XmlSerializer serializer = new XmlSerializer();
		XmlElement elem = serializer.serialize(ids);

		System.out.println(elem);

		Assert.assertEquals(
				"<arrayList><integer>1</integer><integer>1</integer><integer>2</integer><integer>3</integer><integer>5</integer><integer>8</integer></arrayList>",
				elem.toString());

		@SuppressWarnings("unchecked")
		LinkedList<Integer> ids2 = (LinkedList<Integer>) serializer
				.deserialize(elem, LinkedList.class);
		Assert.assertEquals(ids.size(), ids2.size());
	}

	@Test
	public void TestXmlRootElementAnotation() throws Exception {
		ExtendedOwner ext = new ExtendedOwner();
		ext.setId(1L);
		ext.setName("Extended owner");

		XmlSerializer serializer = new XmlSerializer();
		XmlElement elem = serializer.serialize(ext);

		Assert.assertEquals(
				"<owner><extension>nothing verry special about this...</extension><name>Extended owner</name><id>1</id><entityState>NEW</entityState></owner>",
				elem.toString());

		List<ExtendedOwner> owners = new ArrayList<ExtendedOwner>();
		owners.add(ext);

		XmlElement elemList = serializer.serialize(owners);
		System.out.println(elemList);
	}

	@Test
	public void testSubItem() throws Exception {
		Box box = new Box();
		box.setName("Pandora's box");

		BoxedItem item = new BoxedItem();
		item.setName("Hope");
		box.setItem(item);

		XmlSerializer ser = new XmlSerializer();
		XmlElement elem = ser.serialize(box);
		System.out.println(elem.toString());

		Box newBox = (Box) ser.deserialize(elem);
		Assert.assertTrue(box.equals(newBox));

	}

	@Test
	public void testDateTimeFormat() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 5);
		String dateString = String.format("%1$tFT%1$tT", cal);
		String timeZone = String.format("%tz", cal);
		timeZone = timeZone.substring(0, 3) + ":" + timeZone.substring(3);
		dateString += timeZone;
		System.out.println(dateString);
	}

	@Test
	public void testByteArraySerialization() throws Exception {

		// chargement du fichier.
		byte[] data = this.getRessourceBytes("movie1.gif");
		DataContainer container = new DataContainer();
		container.setData(data);

		// sérialization des données
		XmlSerializer serializer = new XmlSerializer();
		serializer.registerClass(DataContainer.class);
		XmlElement elem = serializer.serialize(container);

		// Enregistrement binaire des données
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(out);
		elem.write(osw);
		osw.close();
		out.close();

		// recomposition du xml
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		InputStreamReader isr = new InputStreamReader(in);
		XmlElement elem2 = XmlElement.readXML(isr);
		isr.close();
		in.close();

		Assert.assertEquals(elem.toString(), elem2.toString());

		// d�s�rialisation de l'�l�ment
		DataContainer container2 = serializer.deserialize(elem2,
				DataContainer.class);

		// System.out.println(Hex.encodeHex(data));
		// System.out.println(Hex.encodeHex(container2.getData()));

		Assert.assertArrayEquals(data, container2.getData());
	}

	private byte[] getRessourceBytes(String resName) throws IOException {
		InputStream in = this.getClass().getResourceAsStream(resName);
		final int BUF_SIZE = 10000;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[BUF_SIZE];
		int byteRead = in.read(buf);
		while (byteRead == BUF_SIZE) {
			out.write(buf);
			byteRead = in.read(buf);
		}
		out.write(buf, 0, byteRead);
		in.close();
		out.close();
		return out.toByteArray();
	}

	@Test
	public void TestMessageDeserialization() throws Exception {
		InputStreamReader in = new InputStreamReader(this.getClass()
				.getResourceAsStream("Message.xml"));
		try {
			XmlSerializer ser = new XmlSerializer();
			ser.getClassResolutionPackages().add(
					"org.formix.dsx.serialization.entities2");
			ser.getClassResolutionPackages().add(
					"org.formix.dsx.serialization.entities");
			XmlElement elem = XmlElement.readXML(in);
			ser.deserialize(elem, Message.class);
		} finally {
			in.close();
		}
	}

	@Test
	public void TestMessageSerialization() throws Exception {
		InputStreamReader in = new InputStreamReader(this.getClass()
				.getResourceAsStream("Message.xml"));
		try {
			XmlSerializer ser = new XmlSerializer();
			ser.getClassResolutionPackages().add(
					"org.formix.dsx.serialization.entities2");
			ser.getClassResolutionPackages().add(
					"org.formix.dsx.serialization.entities");
			XmlElement elem = XmlElement.readXML(in);
			Message msg = ser.deserialize(elem, Message.class);

			XmlElement elem2 = ser.serialize(msg);

			System.out.println(elem2.toString());

		} finally {
			in.close();
		}
	}
}
