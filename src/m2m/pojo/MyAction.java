package m2m.pojo;


//import javax.xml.bind.annotation.*;


//@XmlAccessorType(XmlAccessType.FIELD)
public class MyAction {
	String name;
	String key;
	double timeelapsed;
	
	
	
	
	
	
	/**
	 * default Constructor
	 */
	public MyAction() {	}
	
	
	/**
	 * @param name
	 * @param key
	 * @param d
	 */
	public MyAction(String name, String key, double d) {
		super();
		this.name = name;
		this.key = key;
		this.timeelapsed = d;
	}
	/**
	 * @return the name
	 */
//	@XmlElement
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
	 * @return the key
	 */
	//@XmlElement
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the timeelapsed
	 */
//	@XmlElement
	public double getTimeelapsed() {
		return timeelapsed;
	}
	/**
	 * @param timeelapsed the timeelapsed to set
	 */
	public void setTimeelapsed(double timeelapsed) {
		this.timeelapsed = timeelapsed;
	}
	
	
	
	
}
