package m2m.pojo;

public class StereotypeAttribute {
	String name;
	String type;
	String string_value;
	float float_value;
	
	/**
	 * 
	 */
	public StereotypeAttribute() {
	}

	/**
	 * @param name
	 * @param type
	 * @param string_value
	 * @param float_value
	 */
	public StereotypeAttribute(String name, String type, String string_value, float float_value) {
		this.name = name;
		this.type = type;
		this.string_value = string_value;
		this.float_value = float_value;
	}

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the string_value
	 */
	public String getString_value() {
		return string_value;
	}

	/**
	 * @param string_value the string_value to set
	 */
	public void setString_value(String string_value) {
		this.string_value = string_value;
	}

	/**
	 * @return the float_value
	 */
	public float getFloat_value() {
		return float_value;
	}

	/**
	 * @param float_value the float_value to set
	 */
	public void setFloat_value(float float_value) {
		this.float_value = float_value;
	}
	
	
	

}
