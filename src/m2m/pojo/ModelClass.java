/**
 * 
 */
package m2m.pojo;

import java.io.File;
import java.util.ArrayList;

//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Marshaller;
//ssimport javax.xml.bind.annotation.*;

import jakarta.xml.bind.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import m2m.modelreader.MyTransition;

/**
 * @author nigarbutt
 *
 */

@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(propOrder={"name", "stereotype", "multiplicity","stereotype_attributes","actions","assets"})
public class ModelClass {
	String name;
	String stereotype;
	String multiplicity;
	
	//@XmlElementWrapper(name = "stereotype_attributes")
	///@XmlElement(name = "stereotype_attribute")
	@XmlElementWrapper(name = "stereotype_attributes")
	ArrayList<StereotypeAttribute> stereotype_attribute;
	
	@XmlElementWrapper(name = "actions")
	ArrayList<MyAction> action;

	@XmlElementWrapper(name = "assets")
	ArrayList<ModelClass> asset;
	
	MyStateMachine statemachine;
	
	

	
	
	

	
	/**
	 * @return the statemachine
	 */
	public MyStateMachine getStatemachine() {
		return statemachine;
	}




	/**
	 * @param statemachine the statemachine to set
	 */
	public void setStatemachine(MyStateMachine statemachine) {
		this.statemachine = statemachine;
	}




	/**
	 * default constructor
	 */
	public ModelClass() {
	}




	/**
	 * @param name
	 * @param actions
	 */
	public ModelClass(String name, ArrayList<MyAction> actions) {
		this.name = name;
		this.action = actions;
	}




	/**
	 * @return the name
	 */
    //@XmlElement
	public String getName() {
		return name;
	}




	/**
	 * @return the actions
	 */
	public ArrayList<MyAction> getActions() {
		return action;
	}




	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}




	/**
	 * @param actions the actions to set
	 */
	public void setActions(ArrayList<MyAction> actions) {
		this.action = actions;
	}




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




	/**
	 * @return the multiplicity
	 */
	public String getMultiplicity() {
		return multiplicity;
	}




	/**
	 * @param multiplicity the multiplicity to set
	 */
	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}




	/**
	 * @return the stereotype_attributes
	 */
	public ArrayList<StereotypeAttribute> getStereotype_attributes() {
		return stereotype_attribute;
	}




	/**
	 * @param stereotype_attributes the stereotype_attributes to set
	 */
	public void setStereotype_attributes(ArrayList<StereotypeAttribute> stereotype_attributes) {
		this.stereotype_attribute = stereotype_attributes;
	}




	/**
	 * @return the assets
	 */
	public ArrayList<ModelClass> getAssets() {
		return asset;
	}




	/**
	 * @param assets the assets to set
	 */
	public void setAssets(ArrayList<ModelClass> assets) {
		this.asset = assets;
	}




	public static void main(String[] args) {
       
		//ssSystem.setProperty("javax.xml.bind.JAXBContextFactory", "com.sun.xml.internal.bind.v2.ContextFactory");

        MyAction act1 = new MyAction( "flap", "space", 0.33);
        MyAction act2 = new MyAction( "doNothing", "null", 0.1);
        
        ArrayList<MyAction> myactions = new ArrayList<MyAction>();

        myactions.add(act1);
        myactions.add(act2);
        
        ModelClass flappy = new ModelClass("FlappyBird", myactions);
        
        

        try {
            // Create a JAXB context and marshaller
            JAXBContext context = JAXBContext.newInstance(ModelClass.class);
            Marshaller marshaller = context.createMarshaller();

            // Set the marshaller properties
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Write the custom object list to an XML file
            File outputFile = new File("output.xml");
            marshaller.marshal(flappy, outputFile);

            System.out.println("XML file written to " + outputFile.getAbsolutePath());
        } catch (Exception e) {
        	e.printStackTrace();
            System.err.println("Error writing XML file: " + e.getMessage());
        }
    }
	
	
	
}
