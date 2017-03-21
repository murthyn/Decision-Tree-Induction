import java.util.*;

public class Example {

	private String category;
	private ArrayList<String> attributes;
	
	public Example(String cat, ArrayList<String> attrib){
		category = cat;
		attributes = attrib;
	}
	
	public String getCategory(){
		return category;
	}
	
	public ArrayList<String> getAttributes(){
		return attributes;
	}
	
}
