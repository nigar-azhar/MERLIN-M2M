package m2m;

import java.util.ArrayList;
import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import m2m.modelreader.*;
import m2m.pojo.ModelClass;

//import smreader.driver.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.File;
import java.lang.reflect.Constructor;



public class Driver {

	
	public static void print(String s) {
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HashMap<String, Object>  state_instance_map  = new HashMap<>(); 
		ArrayList<Result> responseMatrix = new ArrayList<>();
		System.out.println("WWWWWWWWWWWWWWWWW");
		
		// Reading UML File 
		String umlFilePath = "Examples/FlappyBird.uml";//"Examples/flappy/FlappyBirdGame.uml";////"Examples/MBT_assignment02.uml";
		ModelClass Game =ClassDiagramReader.getClassesFromFile(umlFilePath);
		//String className = StateMachineReader.className;
		
		try {
            // Create a JAXB context and marshaller
            JAXBContext context = JAXBContext.newInstance(ModelClass.class);
            Marshaller marshaller = context.createMarshaller();

            // Set the marshaller properties
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Write the custom object list to an XML file
            File outputFile = new File("flappy.xml");
            marshaller.marshal(Game, outputFile);

            System.out.println("XML file written to " + outputFile.getAbsolutePath());
        } catch (Exception e) {
        	e.printStackTrace();
            System.err.println("Error writing XML file: " + e.getMessage());
        }
	}
	
	public static Object copyObject(Object from) {
		
		Object to = CreateInstanceForClassName(from.getClass().getName(),"");
        Field[] fields = from.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                Field fieldFrom = from.getClass().getDeclaredField(field.getName());
                Object value = fieldFrom.get(from);
                to.getClass().getDeclaredField(field.getName()).set(to, value);
 
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return to;
    }
	
	private static String checkStateInvariant(Object instance, MyState expectedState) {
		ScriptEngineManager scripter = new ScriptEngineManager();
		ScriptEngine engine = scripter.getEngineByName("JavaScript");
		
		
		String script = "";
		
		for (Field f: instance.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			script += f.getName() + " = ";
			Object value = null;
			try {
				value = f.get(instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (f.getType().getName().equals("int") || f.getType().getName().equals("long") || f.getType().getName().equals("double") || f.getType().getName().equals("float"))
					script += value.toString() + " ; ";
			else if (f.getType().getName().equals("java.lang.String"))
				script += "\'"+value.toString()+"\' ; ";
			
		}
		
		script += expectedState.getConstraint();
		System.out.println(script);
		String res = null;
		try {
			res = engine.eval(script).toString();
			System.out.println(res);
		} catch (ScriptException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return res;
		
	}
	
	private static MyState getInitialState(ArrayList<MyState> allmystates) {
		MyState state = null;
		for (MyState st: allmystates) {
			if (st.getStatetype().equalsIgnoreCase("pseudostate"))
				return st;
		}
		
		return state;
	}
	
	
	//"mbt_assignment02."
	public static Object CreateInstanceForClassName(String className, String classPath) {
		Class clazz = null;
		try {
			 clazz = Class.forName(classPath+className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		 Constructor<?> constructor = null;
		    try {
		      constructor = clazz.getConstructor();
		    } catch (NoSuchMethodException | SecurityException e1) {
		      e1.printStackTrace();
		    }
		    Object instance = null;
		    try {
		      instance = constructor.newInstance();
		    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
		        | InvocationTargetException e) {
		      e.printStackTrace();
		    }
		    return instance;
	}
	
	public static void invokeMethod(Object instance, String methodName) {
		 Method method = null;
		    try {
		    	
		      method = instance.getClass().getMethod(methodName);
		      method.setAccessible(true);
		    } catch (NoSuchMethodException | SecurityException e) {
		      e.printStackTrace();
		    }
		    try {
		      method.invoke(instance);
		    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		      e.printStackTrace();
		    }
	}

	
}
