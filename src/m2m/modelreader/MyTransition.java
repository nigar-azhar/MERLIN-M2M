package m2m.modelreader;

public class MyTransition {

	String name;
	//MyState sourceState;
	//MyState targetState;
	
	String sourceName;
	String targetName;
	String guard;
	
	
	
	/**
	 * @return the guard
	 */
	public String getGuard() {
		return guard;
	}
	/**
	 * @param guard the guard to set
	 */
	public void setGuard(String guard) {
		this.guard = guard;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public MyTransition(String name, String sourceName, String targetName) {
		super();
		this.name = name;
		this.sourceName = sourceName;
		this.targetName = targetName;
	}
	/*public MyTransition(String name, MyState sourceState, MyState targetState) {
		super();
		this.name = name;
		this.sourceState = sourceState;
		this.targetState = targetState;
	}*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/*
	public MyState getSourceState() {
		return sourceState;
	}
	public void setSourceState(MyState sourceState) {
		this.sourceState = sourceState;
	}
	public MyState getTargetState() {
		return targetState;
	}
	public void setTargetState(MyState targetState) {
		this.targetState = targetState;
	}*/
	public void printTransition() {
		System.out.println("TransitionName: "+this.name+" :: "
				+ ": "+ this.sourceName+" -> "+ this.targetName);
		
	}
	
}
