import java.util.*;

public class Node {
	private Node parent;
	public ArrayList<Node> children;
	//TODO private ArrayList<Record> data;
	private String testAttribute;
	private String category;
	private String value;
	//private boolean isUsed;
	
	public Node(){
		setParent(null);
		setTestAttribute("");
		children = new ArrayList<Node>();
	}
	
	public Node(Node inputParent){
		setParent(inputParent);
		setTestAttribute("");
		children = new ArrayList<Node>();
		if(inputParent!=null){
			inputParent.children.add(this);
		}
	}
	
	public Node(Node inputParent, String category){
		children = new ArrayList<Node>();
		setParent(inputParent);
		setCategory(category);
		if(inputParent!=null){
			inputParent.children.add(this);
		}
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public void setValue(String v){
		value = v;
	}
	
	public String getValue(){
		return value;
	}

	public Node getParent() {
		return parent;
	}

//	public void setData(ArrayList<Record> data) {
//		this.data = data;
//	}

//	public ArrayList<Record> getData() {
//		return data;
//	}

	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public void setCategory(String cat){
		category = cat;
	}
	
	public String getCategory(){
		return category;
	}

//	public void setUsed(boolean isUsed) {
//		this.isUsed = isUsed;
//	}
//
//	public boolean isUsed() {
//		return isUsed;
//	}

	public void setTestAttribute(String attribute) {
		this.testAttribute = attribute;
	}

	public String getTestAttribute() {
		return testAttribute;
	}
}
