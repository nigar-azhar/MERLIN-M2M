package m2m.modelreader;

public class Result {

	String sourceState;
	String transitionName;
	String OperationName;
	String expectedState;
	//String actualState;
	String passStatus;
	
	
	public Result(String sourceState, String transitionName, String operationName, String expectedState,
			String passStatus) {
		super();
		this.sourceState = sourceState;
		this.transitionName = transitionName;
		OperationName = operationName;
		this.expectedState = expectedState;
		this.passStatus = passStatus;
	}
	public String getSourceState() {
		return sourceState;
	}
	public void setSourceState(String sourceState) {
		this.sourceState = sourceState;
	}
	public String getTransitionName() {
		return transitionName;
	}
	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}
	public String getOperationName() {
		return OperationName;
	}
	public void setOperationName(String operationName) {
		OperationName = operationName;
	}
	public String getExpectedState() {
		return expectedState;
	}
	public void setExpectedState(String expectedState) {
		this.expectedState = expectedState;
	}
	public String getPassStatus() {
		return passStatus;
	}
	public void setPassStatus(String passStatus) {
		this.passStatus = passStatus;
	}
	
	public void printResult() {
		System.out.println(this.sourceState + " :\t: "+this.transitionName + " :\t: "+ this.expectedState+ " :\t: "+this.passStatus);
	}
	
	public static void printResultHeader() {
		System.out.println("Source :\t: Transition :\t: Expected :\t: Result");
	}
	
	
	
	
}
