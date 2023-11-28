package m2m.modelreader;

import java.util.ArrayList;

public class MyState {
	String name;
	String parentstate;
	String statetype;//state, pseudo, final
	String region;
	String constraint;
	ArrayList<MyTransition> outgoingTransitions;
	ArrayList<MyTransition> incomingTransitions;
	boolean iscomposite;
	int numberofregions = 0;
	boolean goesToFinal = false;

	String stereotype;
	
	/**
	 * @return the stereotype
	 */
	public String getStereotype() {
		return stereotype;
	}

	/**
	 * @param stereotype the stereotype to set
	 */
	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}

	public boolean isGoesToFinal() {
		return goesToFinal;
	}

	public void setGoesToFinal(boolean goesToFinal) {
		this.goesToFinal = goesToFinal;
	}

	public MyState(String name, String parentstate, String statetype, String region) {
		super();
		this.name = name;
		this.parentstate = parentstate;
		this.statetype = statetype;
		this.region = region;
		outgoingTransitions = new ArrayList<MyTransition>();
	}
	
	public MyState(String name) {
		super();
		this.name = name;
		outgoingTransitions = new ArrayList<MyTransition>();
	}

	public boolean isComposite() {
		return iscomposite;
	}

	public void setIscomposite(boolean iscomposite) {
		this.iscomposite = iscomposite;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentstate() {
		return parentstate;
	}
	public void setParentstate(String parentstate) {
		this.parentstate = parentstate;
	}
	public ArrayList<MyTransition> getOutgoingTransitions() {
		return outgoingTransitions;
	}
	public void setOutgoingTransitions(ArrayList<MyTransition> outgoingTransitions) {
		this.outgoingTransitions = outgoingTransitions;
	}
	
	public void addOutgoingTransition(MyTransition outgoingTransition) {
		this.outgoingTransitions.add(outgoingTransition);
	}
	public ArrayList<MyTransition> getIncomingTransitions() {
		return incomingTransitions;
	}
	public void setIncomingTransitions(ArrayList<MyTransition> incomingTransitions) {
		this.incomingTransitions = incomingTransitions;
	}
	
	
		public void printMyState() {
			System.out.println("*************************************");
			System.out.println("Name: "+this.name+"\n"
					+ "ParentName: "+ this.parentstate+"\n"
							+ "IsComposite: " + this.isComposite()+"\n"
									+ "Number of regions: "+ this.getNumberofregions()+"\n"
							+ "Region: "+ this.region+"\n"
									+ "Constraint: "+this.constraint+"\n"
							+ "===transitions===");
			for (MyTransition tran: this.outgoingTransitions) {
				tran.printTransition();
				//System.out.println(tran.getName()+" -> "+tran.getTargetState());
			}
		}

		public int getNumberofregions() {
			return numberofregions;
		}

		public void setNumberofregions(int numberofregions) {
			this.numberofregions = numberofregions;
		}

		public String getStatetype() {
			return statetype;
		}

		public void setStatetype(String statetype) {
			this.statetype = statetype;
		}

		public String getConstraint() {
			return constraint;
		}

		public void setConstraint(String constraint) {
			this.constraint = constraint;
		}
	

}
