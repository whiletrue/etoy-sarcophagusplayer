package com.etoy.processing.sarcophagus;


import java.io.File;
import java.io.IOException;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.xml.sax.SAXException;
import org.w3c.dom.*;
import java.util.ArrayList;


public class Playlist {
	
	protected ArrayList<Element> elements;
	protected XPath xp;
	
	public Playlist() {
		
	}
	
	
	public ArrayList<Element> getElements() {
		return elements;
	}
	
	
	public boolean load(String filename) {
		File f = new File(filename);
		return loadFile(f);
	}
	
	
	public boolean loadFile(File file) {
		if (!file.exists() || !file.isFile()) {
			System.out.println("playlist doesn't exist: " + file.getAbsoluteFile());
			return false;
		}
		
		try {
			NodeList elNodes = parseElements(parseFile(file));
			elements = createElements(elNodes);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	protected XPath getXPath() {
		return xp;
	}
	
	protected void setXPath(XPath xpath) {
		xp = xpath;
	}
	
	protected Document parseFile(File file) {
		 try {
			 DocumentBuilder builder = 
				 DocumentBuilderFactory.newInstance().newDocumentBuilder();
		     Document doc = builder.parse(file);
		     setXPath(XPathFactory.newInstance().newXPath());
		     return doc;
		 } catch(ParserConfigurationException e) {
			 System.out.println("ParserConfigurationException: " + e.getMessage());
		 } catch(IOException e){
			 System.out.println("IOException: " + e.getMessage());
		 } catch(SAXException e) {
			 System.out.println("SAXException: " + e.getMessage());
		 }
		return null;
	}
	
	protected NodeList parseElements(Document doc) {
		return doc.getElementsByTagName("element");
	}
	
	
	protected ArrayList<Element> createElements(NodeList elementNodes) {
		return createElements(elementNodes, true);
	}
	
	protected ArrayList<Element> createElements(NodeList elementNodes, boolean propagate) {
		
		int num = elementNodes.getLength();
		
		ArrayList<Element> elements = new ArrayList<Element>();
		Element el;
		System.out.println("Creating " + num + " elements ...");
		for (int i=0; i<num; i++) {
			// create Element
			
			el = createElement(elementNodes.item(i), propagate);
			if (null == el) {
				continue;
			}
			
			elements.add(el);
		}
		
		return elements;
	}
	
	protected Element createElement(Node node) {
		return createElement(node, true);
	}
	
	protected Element createElement(Node node, boolean propagate) {
		
		String eClassName = getNodeValue(node, "type");
		System.out.println("Create Element: " + eClassName);
		if (null == eClassName) {
			return null;
		}
		
		Element el = createElementByType("Element"+eClassName);
		if (propagate) {
			return propagateElement(el, node);
		}
		
		return el;
	}
	
	protected Element propagateElement(Element element, Node node) {
		// x
		element.setPosX(getNodeValueInt(node, "x"));
		// y
		element.setPosY(getNodeValueInt(node, "y"));
		// z-index
		element.setZIndex(getNodeValueInt(node, "z-index"));
		// time offset
		element.setTimeOffset(getNodeValueInt(node, "offset"));
		// duration
		element.setDuration(getNodeValueInt(node, "duration"));
		// repeat
		element.setRepeat(getNodeValueInt(node, "repeat"));
		// name
		element.setName(getNodeValue(node, "name"));
		// transparent
		String ts = getNodeValue(node, "transparent");
		if (ts != null) {
			boolean t =(ts.equals("true")) ? true :false;
			element.setTransparent(t);
		}
		// properties
		return propagateProperties(element, node);
	}
	
	
	protected Element propagateProperties(Element element, Node node) {
		XPath xp = getXPath();
		try {
			NodeList nl = (NodeList) xp.evaluate("properties/entry[@key]", node, XPathConstants.NODESET);
			for (int i=0; i<nl.getLength(); i++) {
				Node n = nl.item(i);
				Node a = n.getAttributes().getNamedItem("key");
				String key = a.getNodeValue();
				if (key!=null && key.length() > 0) {
					element.setProperty(key, n.getFirstChild().getNodeValue());
				}
			}
		
		} catch(Exception e) {
			System.out.println("Ex: " + e.getMessage());
			e.printStackTrace();
		}
		return element;
	}
	
	
	
	protected int getNodeValueInt(Node node, String xpath) {
		String val = getNodeValue(node, xpath);
		if (null == val) {
			return 0;
		}
		return Integer.parseInt(getNodeValue(node, xpath));
	}
	
	protected String getNodeValue(Node node, String xpath) {
		XPath xp = getXPath();
		try {
			Node type = (Node) xp.evaluate(xpath, node, XPathConstants.NODE);
			return type.getFirstChild().getNodeValue();
		} catch(Exception e) {
			System.out.println("XPath eval failed or null: " + xpath);
		}
		return null;
	}
	
	
	protected Element createElementByType(String elemClassName) {
		try {
			Class c = Class.forName("com.etoy.processing.sarcophagus."+elemClassName);
			return (Element) c.newInstance();
			
		} catch(Exception e) {
			
			System.out.println("Exception in createElementByType(): "+e.getMessage());
			e.printStackTrace();
			
		}
		
		//FIXME: appropriate logging ...
		/*
		} catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());	
		} catch(IllegalAccessException e) {
			System.out.println(e.getMessage());	
		} catch(InstantiationException e) {
			System.out.println(e.getMessage());	
		}
		*/
		
		return null;
	}
	
	
	
	
	
}
