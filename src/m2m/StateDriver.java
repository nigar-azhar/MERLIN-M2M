package m2m;

import java.util.ArrayList;
import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import m2m.modelreader.*;

//import smreader.driver.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;



public class StateDriver {

	
	public static void print(String s) {
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HashMap<String, Object>  state_instance_map  = new HashMap<>(); 
		ArrayList<Result> responseMatrix = new ArrayList<>();
		System.out.println("WWWWWWWWWWWWWWWWW");
		
		// Reading UML File 
		String umlFilePath = "Examples/FlappyBird.uml";//"Examples/MBT_assignment02.uml";
		ArrayList<MyState> allmystates =StateMachineReader.getStateMachineStatesFromFile(umlFilePath);
		String className = StateMachineReader.className;
		System.out.println("//======================\\\\");
		System.out.println("//======================\\\\");
		System.out.println("//======================\\\\");
		
		
		
		// Getting initial state from sm
		
		MyState currentState = getInitialState(allmystates);
		
		print(currentState.getName());
		print(currentState.getStatetype());
		MyTransition transition = currentState.getOutgoingTransitions().get(0);
		
		print(transition.getName());
		
		
		MyState expectedState = StateMachineReader.findStateByName(allmystates, transition.getTargetName());
		
		// Initializing Object of Class
		
		Object instance = CreateInstanceForClassName(className, "mbt_assignment02.");
		String passStatus = checkStateInvariant(instance, expectedState);
		responseMatrix.add(new Result( currentState.getName(), transition.getName(), transition.getName(), expectedState.getName(), passStatus));
		
		if (passStatus.equals("true"))
			state_instance_map.put(expectedState.getName(),copyObject(instance));
		
		for (MyState state: allmystates) {
			if (state.getStatetype().equalsIgnoreCase("state")) {
				System.out.println("line 59"+state.getName());

				if (state_instance_map.containsKey(state.getName())) {
					System.out.println("line 62"+state.getName());

					Object inst = state_instance_map.get(state.getName());
			
					for(MyTransition trans: state.getOutgoingTransitions()) {
						
						expectedState = StateMachineReader.findStateByName(allmystates, trans.getTargetName());
						if (expectedState.getStatetype().equalsIgnoreCase("state")) {
							Object inst_2 = copyObject(inst);
							invokeMethod(inst_2, trans.getName());
							
							passStatus = checkStateInvariant(inst_2, expectedState);
							
							responseMatrix.add(new Result(state.getName(), trans.getName(), trans.getName(), expectedState.getName(), passStatus));
							if (passStatus.equalsIgnoreCase("true"))
								if (state_instance_map.containsKey(expectedState.getName()) == false) {
									state_instance_map.put(expectedState.getName(),inst_2);
									System.out.println("here");
	
								}
						}
					}
					
				}
			}
		}
		System.out.println("//================================================\\\\");
		
		Result.printResultHeader();
		for (Result res: responseMatrix) {
			res.printResult();
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
