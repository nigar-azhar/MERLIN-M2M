package m2m.pojo;

import java.util.ArrayList;

import m2m.modelreader.MyState;

public class MyStateMachine {
	String name;
	String parent;
	ArrayList<MyState> state;
	
	
	public MyStateMachine() {}
	
	
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}
	/**
	 * @return the allmystates
	 */
	public ArrayList<MyState> getAllmystates() {
		return state;
	}
	/**
	 * @param allmystates the allmystates to set
	 */
	public void setAllmystates(ArrayList<MyState> allmystates) {
		this.state = allmystates;
	} 
	
	
	
}
