import java.util.*;

public class Driver {
	public static int numAttributes;
	public static ArrayList<String> attributeList = new ArrayList<String>();
	public static ArrayList<String> possibleCategoryList = new ArrayList<String>();
	public static HashMap<String, ArrayList<String>> possibleAttributeList = new HashMap<String, ArrayList<String>>();
	public static ArrayList<Example> trainingExamples = new ArrayList<Example>();
	public static ArrayList<Example> testingExamples = new ArrayList<Example>();
	
	private static int maxDepth = 0;
	private static int numNodes = 0;
	
	public static void main(String[] args){
//		SimpleFile file = new SimpleFile("tennis.txt");
		SimpleFile file = new SimpleFile("mushrooms.txt");
		
		//REMEMBER TO REMOVE RANDOM SPLITTING FOR TENNIS SAMPLE
		
		ArrayList<String> tempList = null;
		int counter = 0;
		ArrayList<String> lineList  = new ArrayList<String>();
		for (String line : file){
			if(counter == 0){
				String[] attrList= line.split(",");
				for(String i : attrList){
					attributeList.add(i);
				}
				attributeList.remove(0);
				numAttributes = attributeList.size();
				
				//TODO Do something here
			}
			else lineList.add(line);
			counter++;
		}
		int selected = 0;
		Random rand = new Random();
//		System.out.println("Training Dataset");
		int maxSelected = (int) (lineList.size() / 60);
		//TODO FIX ABOVE LINE WHEN DOING BIGGER DATA SETS
//		int maxSelected = lineList.size();
		while(selected < maxSelected){
			int choice = rand.nextInt(lineList.size());
			String[] lineSplit = lineList.get(choice).split(","); 
			ArrayList<String> attrib = new ArrayList<String>();
			for(int i = 1; i < lineSplit.length; i++){
				attrib.add(lineSplit[i]);
				if(possibleAttributeList.containsKey(attributeList.get(i-1))){
					tempList = possibleAttributeList.get(attributeList.get(i-1));
					if(tempList == null){
						tempList = new ArrayList();
					}
					if(!tempList.contains(lineSplit[i])) tempList.add(lineSplit[i]);  
				} 
				else {
				      tempList = new ArrayList();
				      tempList.add(lineSplit[i]);               
				}
				possibleAttributeList.put(attributeList.get(i-1),tempList);
			}

			trainingExamples.add(new Example(lineSplit[0], attrib));
//			System.out.println("Category: " + lineSplit[0] + " and Attributes: " + attrib);
			if(!possibleCategoryList.contains(lineSplit[0])) possibleCategoryList.add(lineSplit[0]);
			lineList.remove(choice);
			selected ++;
		}

//		System.out.println("Testing Dataset");
		for(String remainingLine : lineList){
			String[] lineSplit = remainingLine.split(","); 
			ArrayList<String> attrib = new ArrayList<String>();
			for(int i = 1; i < lineSplit.length; i++){
				attrib.add(lineSplit[i]);
				if(possibleAttributeList.containsKey(attributeList.get(i-1))){
					tempList = possibleAttributeList.get(attributeList.get(i-1));
					if(tempList == null){
						tempList = new ArrayList();
					}
					if(!tempList.contains(lineSplit[i])) tempList.add(lineSplit[i]);  
				}
				else {
				      tempList = new ArrayList();
				      tempList.add(lineSplit[i]);               
				}
			   possibleAttributeList.put(attributeList.get(i-1),tempList);
			}
			testingExamples.add(new Example(lineSplit[0], attrib));
			if(!possibleCategoryList.contains(lineSplit[0])) possibleCategoryList.add(lineSplit[0]);
//			System.out.println("Category: " + lineSplit[0] + " and Attributes: " + attrib);	
		}
		
//		System.out.println("TOTAL ENTROPY: " + calculateEntropy(trainingExamples));
		
//		testingExamples = trainingExamples; //TODO REMOVE THIS
		Node root = ID3(trainingExamples, attributeList, null, null);
//		System.out.println("----PROGRAM DONE----");
		
		System.out.println("TREE: ");
		printTree(root, 0);
		
		
		int successCount = 0;
		int totalCount = 0;
		System.out.println("Testing accuracy: ");
		for(Example example : testingExamples){
//			System.out.println(example.getAttributes() + ":" + example.getCategory());
			if(testSuccess(root, example)){
				successCount++;
//				System.out.println(example.getAttributes() + example.getCategory() + " SUCCESSFUL");
			}
			else{
//				System.out.println(example.getAttributes() + example.getCategory() + " FAILED");
			}
			totalCount++;
		}
//		System.out.println("Success: " + successCount/((double) totalCount));
////		System.out.println("Training Size: " + trainingExamples.size());
////		System.out.println("Testing Size: " + testingExamples.size());
//		System.out.println("Training:Testing Ratio: " + trainingExamples.size()/((double)testingExamples.size()));
//		System.out.println("Depth of tree: " + maxDepth);
//		System.out.println("Number of nodes: " + numNodes);
		
		//For purpose of easy recording:
		System.out.println(trainingExamples.size()/((double)testingExamples.size()));
		System.out.println(maxDepth);
		System.out.println(numNodes);
		System.out.println(successCount/((double) totalCount));
		
	}
	
	public static boolean testSuccess(Node root, Example example){
		String testAttrRoot = root.getTestAttribute();
		int index = 0;
		for(int i = 0; i < attributeList.size(); i++){
			if(attributeList.get(i).equals(testAttrRoot)){
				index = i;
				break;
			}
		}
		String attributeValExample = example.getAttributes().get(index);
		for(Node child : root.getChildren()){
			if(child.getValue().equals(attributeValExample)){
				if(child.getTestAttribute() != null){
					return testSuccess(child, example);
				}
				else{
					return (child.getCategory().equals(example.getCategory()));
				}
			}
			
		}
		
		//Find the attribute in examplelist that corresponds to root's attribute
		//Find root's child that has same value
		//Continue down the line until root's child does not have a test attribute
		//If example's category matches that of the leaf, then return true
		//Else, return false
		
		return false;
	}
	
	public static void printTree(Node rootNode, int level){
		numNodes ++;
		String empty = "";
		for(int i = 0; i < level; i++){
			empty += "   ";
			if(level > maxDepth) maxDepth = level;
		}
		if(rootNode.getTestAttribute() == null){
			System.out.println(empty + "If it is " + rootNode.getValue() + " then " + rootNode.getCategory());
			return;
		}
		if(rootNode.getValue()==null)System.out.println(empty + "Check the " + rootNode.getTestAttribute());
		else System.out.println(empty + "If it is " + rootNode.getValue() + " then check the " + rootNode.getTestAttribute());
		for(Node child : rootNode.getChildren()){
			printTree(child, level+1);
		}
	}
	
	public static Node ID3(ArrayList<Example> examples, ArrayList<String> attributes, Node cNode, String val){
//		if(val==null)System.out.println("VALUE IS NULL");
		
//		System.out.println("------------------------------");
//		for(Example i : examples){
//			System.out.println(i.getAttributes());
//		}
//		System.out.println("----");
//		System.out.println("Attributes: " + attributes);
		
		if(sameCategory(examples)){
//			System.out.println("SAME CATEGORY");
			Node newNode = new Node(cNode, examples.get(0).getCategory());
			newNode.setValue(val);
			return newNode;
		}
		String highestCategory;
		if(attributes.isEmpty()){
//			System.out.println("Attributes IS EMPTY");
			highestCategory = highestCategory(examples);
			Node newNode = new Node(cNode, highestCategory);
			newNode.setValue(val);
			return newNode;
		}	
		double highestGain = 0;
		String highestAttribute = ""; //TODO ADDED THIS LINE
		for(String attribute : attributes){
			HashMap<String, ArrayList<Example>> sublists = findSublists(examples, attribute);
			ArrayList<Double> subEntropies = new ArrayList<Double>();
			ArrayList<Integer> subSizes = new ArrayList<Integer>();
//			System.out.println("sublists: " + sublists);
			for(String attributeVal : sublists.keySet()){
				subEntropies.add(calculateEntropy(sublists.get(attributeVal)));
				subSizes.add(sublists.get(attributeVal).size());
//				System.out.println("AttributeVal: " + attributeVal);
//				System.out.println("subSizes: " + subSizes);
//				System.out.println("SubEntropies: " + subEntropies);
			}
			double calculatedGain = calculateGain(calculateEntropy(examples), subEntropies, subSizes, examples.size());
//			System.out.println("Gain: " + calculatedGain + " Attribute: " + attribute);
			if(calculatedGain >= highestGain){
				highestGain = calculatedGain;
				highestAttribute = attribute;
			}
		}
//		System.out.println("******Selected highest attribute*****: " + highestAttribute);
		Node newnode = new Node(cNode); //Not a leaf nod
		newnode.setValue(val);
		newnode.setTestAttribute(highestAttribute);
		
		ArrayList<Example> nextExamples = new ArrayList<Example>();
		ArrayList<String> attributesNext = new ArrayList<String>(attributes);
//		System.out.println("Going through Values: " + possibleAttributeList.get(highestAttribute));
		for(String v : possibleAttributeList.get(highestAttribute)){
//			System.out.println("Value: " + v);
			boolean ExamplesV = false;
			for(Example example : examples){
				for(int i = 0; i < attributeList.size(); i ++){
					if(example.getAttributes().get(i).equals(v)){
						ExamplesV = true;
						nextExamples.add(example);
					}
				}
			}
			Node childnode;
			if(!ExamplesV){
//				System.out.println("No examples with value: " + v);
				String highestCategory2 = "";
				highestCategory2 = highestCategory(examples);
				childnode = new Node(newnode, highestCategory2);
//				System.out.println("Returning childnode with highest category: " + highestCategory2);
				childnode.setValue(v);
				return childnode;	
			}
			attributesNext.remove(highestAttribute);
//			System.out.println("Next Attributes: " + attributesNext);
//			System.out.println("Next Examples: ");
//			for(Example i : nextExamples){
//				System.out.println("Category: " + i.getCategory() + " and Attributes: " + i.getAttributes());	
//			}	
			childnode = ID3(nextExamples, attributesNext, newnode, v);
			childnode.setValue(v); //TODO CHANGE THIS PROBABLY BAD? USED TO BE UNCOMMENTED
			nextExamples.clear();
		}
//		System.out.println("EXITING FUNCTION: " + newnode.getTestAttribute());
		return newnode;
	}
	
	public static String highestCategory(ArrayList<Example> examples){
		int count = 0;
		HashMap<String, Integer> countCategory = new HashMap<String, Integer>();
		for (Example i : examples){
			if(countCategory.containsKey(i.getCategory())){
				count = countCategory.get(i.getCategory());
				count++;
				countCategory.put(i.getCategory(), count);
				
			} 
			else {
				countCategory.put(i.getCategory(), 1);
			}
		}
		int highestValue = 0;
		String highestCategory = "";
		for(String categoryMap : countCategory.keySet()){
			if(countCategory.get(categoryMap) > highestValue){
				highestValue = countCategory.get(categoryMap);
				highestCategory = categoryMap;
			}
		}
		return highestCategory;
	}
	
		
	
	public static boolean sameCategory(ArrayList<Example> examples){
		String category = examples.get(0).getCategory();
		for(Example i : examples){
			if(!i.getCategory().equals(category)){
				return false;
			}
		}
		return true;
	}
	
	public static double calculateEntropy(ArrayList<Example> data){
		double entropy = 0;
		if(data.size() == 0){
			return entropy;
		}
		
		double count = 0;
		HashMap<String, Double> countCategory = new HashMap<String, Double>();
		for (Example i : data){
			if(countCategory.containsKey(i.getCategory())){
				count = countCategory.get(i.getCategory());
				count++;
				countCategory.put(i.getCategory(), count);
			} 
			else {
				countCategory.put(i.getCategory(), 1.0);
			}
		}
		
		for(String cat : countCategory.keySet()){
			entropy = entropy - (countCategory.get(cat)/data.size())*(Math.log10(countCategory.get(cat)/data.size())/(Math.log10(2)));
		}
		return entropy;
	}
	
	//TODO FIX THIS
	public static double calculateGain(double rootEntropy, ArrayList<Double> subEntropies, ArrayList<Integer> setSizes, int data) {
		double gain = rootEntropy;
//		System.out.println("Within calculate gain: Root Entropy " + rootEntropy + " setSizes: " + setSizes + " subEntropies: " + subEntropies + " data: " + data);
		
		for(int i = 0; i < subEntropies.size(); i++) {
			gain += -((setSizes.get(i) / (double)data) * subEntropies.get(i));
		}
		
		return gain;
	}
	
	public static HashMap<String, ArrayList<Example>> findSublists(ArrayList<Example> examples, String attribute){
		HashMap<String, ArrayList<Example>> sublists = new HashMap<String, ArrayList<Example>>();
		for(String attributeValue : possibleAttributeList.get(attribute)){
			for(Example example : examples){
				for(int i = 0; i < attributeList.size(); i ++){
					if(attributeList.get(i).equals(attribute)){
						if(example.getAttributes().get(i).equals(attributeValue)){
							if(sublists.containsKey(attributeValue)){
								ArrayList<Example> listOfExamplesWithAttribute = sublists.get(attributeValue);
								listOfExamplesWithAttribute.add(example);
							}
							else{
								ArrayList<Example> listOfExamplesWithAttribute = new ArrayList<Example>();
								listOfExamplesWithAttribute.add(example);
								sublists.put(attributeValue, listOfExamplesWithAttribute);
							}
						}
					}
				}
			}
		}
		return sublists;
	}
}
