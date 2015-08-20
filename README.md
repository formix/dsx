# dsx (Damn Simple Xml for Java)

This library leverage method chaining. It is not compliant with the 
Java XML Standard API. It contains a serializer compatible with 
Microsoft XmlSerializer input/output XML format. It contains another 
Microsoft compatible serializer that behave like the 
DataContractSerializer class. Along with the basic soap package provided, 
it becomes easy to do raw SOAP calls to any SOAP Web service. The XmlDocument 
class also offer XPath like methods to fetch information from the 
underlying XML tree.

Consult the complete api documentation [here](http://formix.github.io/dsx/).

# Installation

Maven dependency:

```xml
<dependencies>
	<dependency>
		<groupId>org.formix</groupId>
		<artifactId>dsx</artifactId>
		<version>0.10.0</version>
	</dependency>
	[...]
</dependencies>
```