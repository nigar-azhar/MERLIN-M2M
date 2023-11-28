package m2m.modelreader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.uml2.uml.internal.impl.OpaqueExpressionImpl;


/**
 * A class that reads UML class diagram in XMI format and prints its elements.
 * 
 * @author Nigar Azhar Butt
 * @version 1.0
 */
public class StateMachineReader {

	public static ArrayList<MyState> allmystates = new ArrayList<>();
	public static ArrayList<MyTransition> allmytransition = new ArrayList<>();
	public static String className;
	
	//String umlFilePath = "Examples/psm.uml";
	public static ArrayList<MyState> getStateMachineStatesFromFile(String umlFilePath ){
		 allmystates = new ArrayList<>();
		 allmytransition = new ArrayList<>();
		ModelLoader umlModel = new ModelLoader();
		
		String uri = null;
		try {
			uri = umlModel.getFileURI(umlFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object objModel = umlModel.loadModel(uri);
		Model sourceModel;
		EList<PackageableElement> sourcePackagedElements = null;
		if (objModel instanceof Model) {
			sourceModel = (Model) objModel;
			sourcePackagedElements = sourceModel.getPackagedElements();
		} else if (objModel instanceof Package) {
			Package sourcePackage = (Package) objModel;
			sourcePackagedElements = sourcePackage.getPackagedElements();
		}

		for (PackageableElement element : sourcePackagedElements){
			//for nested package
			if(element.eClass() == UMLPackage.Literals.PACKAGE){
				org.eclipse.uml2.uml.Package nestedPackage = (org.eclipse.uml2.uml.Package) element;
				EList<PackageableElement> nestedPackagedElements = nestedPackage.getPackagedElements();
				for (PackageableElement nestedElement : nestedPackagedElements){
					printModelDetails(nestedElement);
				}
			}
			else
				printModelDetails(element);
		}
		return allmystates;
	}
	
	public static void main(String[] args) {
		ModelLoader umlModel = new ModelLoader();
		String umlFilePath = "Examples/psm.uml";
		String uri = null;
		try {
			uri = umlModel.getFileURI(umlFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object objModel = umlModel.loadModel(uri);
		Model sourceModel;
		EList<PackageableElement> sourcePackagedElements = null;
		if (objModel instanceof Model) {
			sourceModel = (Model) objModel;
			sourcePackagedElements = sourceModel.getPackagedElements();
		} else if (objModel instanceof Package) {
			Package sourcePackage = (Package) objModel;
			sourcePackagedElements = sourcePackage.getPackagedElements();
		}

		for (PackageableElement element : sourcePackagedElements){
			//for nested package
			if(element.eClass() == UMLPackage.Literals.PACKAGE){
				org.eclipse.uml2.uml.Package nestedPackage = (org.eclipse.uml2.uml.Package) element;
				EList<PackageableElement> nestedPackagedElements = nestedPackage.getPackagedElements();
				for (PackageableElement nestedElement : nestedPackagedElements){
					printModelDetails(nestedElement);
				}
			}
			else
				printModelDetails(element);
		}
		System.out.println("/////////////////////////////");
		for (MyState state: allmystates) {
			state.printMyState();
		}
		System.out.println("/////////////////////////////");
		for (MyTransition tran: allmytransition) {
			tran.printTransition();
			
		}
		
		System.out.println("/////////////////////////////");
		
		ArrayList<MyState> sts= flattenStates(allmystates, allmytransition);
		
		for (MyState state: sts) {
			state.printMyState();
		}
		
		savemodel(convertToModel(sts));
		
		
	}

	private static void printModelDetails(PackageableElement element){
		if (element.eClass() == UMLPackage.Literals.CLASS)
		{
			System.out.println("\n----------Class Details----------");
			Class clas = (Class)element;
			
			//get class name
			String clasName = clas.getName();
			System.out.println("Class Name: "+clasName);
			className = clasName;
			//get class attributes
			EList<Property> attributes = clas.getOwnedAttributes();				
			System.out.println("Attributes: ");
			if(!attributes.isEmpty()){
				for (Property attr: attributes)
				{
					System.out.println(attr.getName());
				}
			}
			//state machine as class owned behavior
			EList<Behavior> ownedBehavior = clas.getOwnedBehaviors();
			for (Behavior beh: ownedBehavior)
			{
				System.out.println("\n----------State Machine Details----------");
				if(beh.eClass() == UMLPackage.Literals.STATE_MACHINE)
				{
					StateMachine sm = (StateMachine)beh;
					readSM(sm);
					
				}
			}
			
		}
		//for a stand-alone state machine
		if (element.eClass() == UMLPackage.Literals.STATE_MACHINE) {
			System.out.println("here");
			readSM( (StateMachine) element);
		}
	}
	
	static public void readState(State s, State ps, String region) {
		
		 ArrayList<MyTransition> allstatetransition = new ArrayList<>();

		
		System.out.println("State: "+s.getName());
		//sss.getAppliedStereotypes().get(0).get
		//String stero = s.getAppliedStereotypes().get(0).getLabel();
		for(Stereotype st: s.getAppliedStereotypes()) {
			String stero = st.getLabel();

			System.out.println(stero);
		}
		
		System.out.println("------Outgoing Transitions------");
		EList<Transition> outgoingTransitions=s.getOutgoings();
		for (Transition transition: outgoingTransitions)
		{	
			// assuming you have a transition object called "transition"
			Constraint guard = transition.getGuard();
			String guardst = "";
			if (guard != null) {
			    String guardExpression = guard.getSpecification().stringValue();
			    System.out.println("Guard condition: " + guardExpression);
			    guardst = guardExpression;
			} else {
			    System.out.println("No guard condition defined.");
			}
			
			
//			System.out.println("Transition Name: "+transition.getName());
//			for (Constraint constraint: transition.getOwnedRules()) {
//				System.out.println(constraint.getSpecification().getLabel());
//			}
//				//sSystem.out.println(transition.getOwnedRules().get(0).getSpecification().getLabel());
			MyTransition tran = new MyTransition(transition.getName(),s.getName(), transition.getTarget().getName());
			tran.setGuard(guardst);
			tran.printTransition();
			allstatetransition.add(tran);
			allmytransition.add(tran);
		}
		
		//boolean iscomposite = false;;
		// Nested Regions
		if (s.getRegions() != null) {
		//	iscomposite = true; 
			EList<Region> nestedregions = s.getRegions();
			for (Region nreg : nestedregions) { // parallel regions
				//read nested states
				EList<Vertex> vertices = nreg.getSubvertices();
				for(Vertex vertex: vertices)
				{
					ArrayList<MyTransition> allstatetransition2 = new ArrayList<>();
					
					if(vertex.eClass() == UMLPackage.Literals.STATE)
					{
						State s2 = (State)vertex;
						readState(s2, s, nreg.getName());
					}
					else if(vertex.eClass() == UMLPackage.Literals.PSEUDOSTATE){
						Pseudostate s2 = (Pseudostate)vertex;
						System.out.println("Pseudostate: "+s2.getName());
						System.out.println("------Outgoing Transitions------");
						EList<Transition> outgoingTransitions1=s2.getOutgoings();
						for (Transition transition: outgoingTransitions1)
						{	
							
							MyTransition tran = new MyTransition(transition.getName(),s2.getName(), transition.getTarget().getName());
							tran.printTransition();
							allstatetransition2.add(tran);
							allmytransition.add(tran);
						}
						
						MyState state = null;
						//if (s!= null)
							state = new MyState(s2.getName(),s.getName(), "pseudostate", nreg.getName());
							state.setOutgoingTransitions(allstatetransition2);
						//else
						//	state = new MyState(s2.getName(),null, "pseudostate", nreg.getName());
						
						allmystates.add(state);
					
					}
					else if(vertex.eClass() == UMLPackage.Literals.FINAL_STATE){
						FinalState s2 = (FinalState)vertex;
						System.out.println("FinalState: "+s2.getName());
						MyState state = null;
						//if (s!= null)
							state = new MyState(s2.getName(),s.getName(), "finalstate", nreg.getName());
						//else
						//	state = new MyState(s2.getName(),null, "finalstate", nreg.getName());
						allmystates.add(state);
					}
				}
			}
		}
		
			

			System.out.println("\n---+++-;;;;;----");
			MyState state = null;
			if (ps!= null)
				state = new MyState(s.getName(),ps.getName(), "state", region);
			else
				state = new MyState(s.getName(),null, "state", region);

			ValueSpecification vs = null;
			if (s.getOwnedRules().size() > 0)
			{	//state.setConstraint(s.getOwnedRules().get(0));
			
				 vs = s.getOwnedRules().get(0).getSpecification();
				 state.setConstraint(vs.stringValue());
			}
			//state.setConstraint();
			
			state.setOutgoingTransitions(allstatetransition);
			state.setIscomposite(s.isComposite());
			if (s.getRegions() == null)
				state.setNumberofregions(0);
			else
				state.setNumberofregions(s.getRegions().size());
			state.setStereotype("-");
			allmystates.add(state);
		
		
		
	}
	static public void readSM(StateMachine sm_) {
		System.out.println("\n---+++-----State Machine Details----------");
		if(sm_.eClass() == UMLPackage.Literals.STATE_MACHINE)
		{
			StateMachine sm = (StateMachine)sm_;

			EList<Region> regions = sm.getRegions();
			for(Region reg: regions)
			{
				EList<Vertex> vertices = reg.getSubvertices();
				for(Vertex vertex: vertices)
				{
					
					if(vertex.eClass() == UMLPackage.Literals.STATE)
					{
						
						
						State s = (State)vertex;
						readState(s, null, reg.getName());
						
						
						
					}
					else if(vertex.eClass() == UMLPackage.Literals.PSEUDOSTATE){
						Pseudostate s = (Pseudostate)vertex;
						System.out.println("Pseudostate: "+s.getName());
						
						System.out.println("------Outgoing Transitions------");
						EList<Transition> outgoingTransitions=s.getOutgoings();
						ArrayList<MyTransition> allstatetransition2 = new ArrayList<>();
						for (Transition transition: outgoingTransitions)
						{	
							
							MyTransition tran = new MyTransition(transition.getName(),s.getName(), transition.getTarget().getName());
							tran.printTransition();
							allstatetransition2.add(tran);
							allmytransition.add(tran);
						}
						
						
						MyState state = new MyState(s.getName(),null, "pseudostate", reg.getName());
						state.setOutgoingTransitions(allstatetransition2);
						allmystates.add(state);
					}
					else if(vertex.eClass() == UMLPackage.Literals.FINAL_STATE){
						FinalState s = (FinalState)vertex;
						System.out.println("FinalState: "+s.getName());
						MyState state = null;
						
						state = new MyState(s.getName(),null, "finalstate", reg.getName());
					
					allmystates.add(state);
					}
				}
			}
		}
	}
	public static void savemodel( Model model_) {
		  String umlFilePath =
				  "Examples/som4.uml";
				  
		  Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		  Map<String, Object> m = reg.getExtensionToFactoryMap();
		  m.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new
		  XMIResourceFactoryImpl()); 
		  //File f = new File(umlFilePath);
		  Resource resource = new ResourceSetImpl().createResource(URI.createFileURI(umlFilePath));
		  resource.getContents().add(model_); try { resource.save(null); } catch
		  (IOException ioe) {}
	  }
	
	public static MyState getPseudoStateInRegion(ArrayList<MyState> allstates) {
		
		for (MyState state: allstates) {
			if (state.statetype.equalsIgnoreCase("pseudostate"))
				return state;
		}
		return null;
	}
	
	public static MyState getFinalStateInRegion(ArrayList<MyState> allstates) {
		
		for (MyState state: allstates) {
			if (state.statetype.equalsIgnoreCase("finalstate"))
				return state;
		}
		return null;
	}
	
	
	public static ArrayList<MyState> getOrthogonalState(ArrayList<MyState> allstates, MyState pstate){
		ArrayList<MyState> orthostates = new ArrayList<>();
		ArrayList<MyState> region1 = new ArrayList<>();
		ArrayList<MyState> region2 = new ArrayList<>();
		for (MyState state: allstates) {
			if (state.getParentstate()!= null && state.getParentstate().equalsIgnoreCase(pstate.name)) {
				if (state.getRegion().equalsIgnoreCase("Region1")) {
					region1.add(state);
				}
				else if(state.getRegion().equalsIgnoreCase("Region2")) {
					region2.add(state);
				}
			}
		}
		
		
		///////////// 
		// INITIAL STATE
		MyState init1 = getPseudoStateInRegion(region1);
		MyState init2 = getPseudoStateInRegion(region2);
		
		MyState newInitState = new MyState(init1.getName(), init1.getParentstate(), init1.statetype, init1.getRegion());
		MyTransition temp1 = init1.getOutgoingTransitions().get(0);
		MyTransition temp2 = init2.getOutgoingTransitions().get(0);
 		MyTransition initTrans = new MyTransition(temp1.getName(),newInitState.getName(), temp1.getTargetName()+"-"+temp2.targetName );
 		ArrayList<MyTransition> mylist = new ArrayList<>();
 		mylist.add(initTrans);
 		newInitState.setOutgoingTransitions(mylist);
 		orthostates.add(newInitState);
 		
 		
 		
		for (MyState r1st: region1) {
			if (r1st.statetype.equalsIgnoreCase("state") ) {
				for (MyState r2st: region2) {
					if (r2st.statetype.equalsIgnoreCase("state") ) {
					
						MyState newState = new MyState(r1st.getName()+"-"+r2st.getName());
						newState.setParentstate(r1st.getParentstate());
						newState.setRegion(r1st.getRegion());
						newState.setConstraint(r1st.getConstraint() + " and "+ r2st.getConstraint());
						newState.statetype = "state";
						ArrayList<MyTransition> newtranslist = new ArrayList<>();
						for (MyTransition trans: r1st.getOutgoingTransitions()) {
							MyState target = findStateByName(allstates, trans.getTargetName());
							if (target.statetype.equalsIgnoreCase("finalstate")== false) {
								MyTransition newtrans = new MyTransition(trans.getName(), newState.getName(), trans.getTargetName()+"-"+r2st.getName());
								newtranslist.add(newtrans);
							}
							else
								newState.setGoesToFinal(true);
						}
						
						
						for (MyTransition trans: r2st.getOutgoingTransitions()) {
							MyState target = findStateByName(allstates, trans.getTargetName());
							if (target.statetype.equalsIgnoreCase("finalstate")== false) {
								MyTransition newtrans = new MyTransition(trans.getName(), newState.getName(), r1st.getName()+"-"+trans.getTargetName());
								newtranslist.add(newtrans);
							}
							else
								newState.setGoesToFinal(true);
						}
						newState.setOutgoingTransitions(newtranslist);
						orthostates.add(newState);
					}
				}
			}
			/*else if (r1st.statetype.equalsIgnoreCase("pseudostate")) {
				MyState newState = new MyState(r1st.getName(), r1st.getParentstate(), r1st.statetype, r1st.getRegion());
			}*/
		}
		MyState temp3 = getFinalStateInRegion(region1);
		MyState newfinalState = new MyState(temp3.getName(), temp3.getParentstate(), temp3.statetype, temp3.getRegion());
		orthostates.add(newfinalState);
		
		for (MyState state: orthostates) {
			if(state.isGoesToFinal() == true) {
				MyTransition temptrans = new MyTransition(null,state.getName(), newfinalState.getName() );
				state.addOutgoingTransition(temptrans);
			}
		}
		
		
		
		
		
		return orthostates;
	}
	
	public static ArrayList<MyState> flattenStates(ArrayList<MyState> allunflatstates, ArrayList<MyTransition> oldtransitions){
		 ArrayList<MyState> singleregionstates = new ArrayList<>();
		 for (MyState state: allunflatstates ) {
			 if (state.iscomposite == false && state.getParentstate() == null ) {
				 singleregionstates.add(state);
			 }
			 else if (state.numberofregions <= 1  && state.getParentstate() == null ) {
				 singleregionstates.add(state);
			 } 
			 else if (state.iscomposite == true && state.numberofregions > 1 ) {
				 
				 singleregionstates.addAll(getOrthogonalState(allunflatstates, state));
				 singleregionstates.add(state);
			 }
			 
			 
			 
		 }
		
		
		
		
		 allunflatstates = singleregionstates;
		
		
		
		
		ArrayList<MyState> flatstates = new ArrayList<>();
		 for (MyState state: allunflatstates ) {
			 MyState newState = new MyState(state.getName());
			 newState.statetype = state.statetype;
			 
			 if(state.statetype.equalsIgnoreCase("finalstate") && state.parentstate != null) {
				 // ignore all final states that are nested
				 System.out.println("#################\n\n"+ state.name+"\n###############\n");
			 }
			 
			 else if (state.statetype.equalsIgnoreCase("pseudostate") && state.parentstate == null) {
				 
				 ArrayList<MyTransition> transNewState = new ArrayList<>();
				 for (MyTransition tran: state.getOutgoingTransitions()) {
					 MyState target = findStateByName(allunflatstates,tran.getTargetName());
					 if (target.iscomposite == true) {
						 MyState target2 = findInitialOfComposite(allunflatstates,target);
						 transNewState.add(new MyTransition(tran.getName(),tran.getSourceName(), target2.getName()));
					 }
					 else
						 transNewState.add(tran);
				 }
				 newState.setOutgoingTransitions(transNewState);
				 flatstates.add(newState);
			 }
			 
			 
			 
			 else  if (state.iscomposite == false && state.statetype.equalsIgnoreCase("pseudostate") == false ) {
				 ArrayList<MyTransition> transNewState = new ArrayList<>();
				 for (MyTransition tran: state.getOutgoingTransitions()) {
					 
					 MyState target = findStateByName(allunflatstates,tran.getTargetName());
					 if (target.iscomposite == true) {
						 MyState target2 = findInitialOfComposite(allunflatstates,target);
						 transNewState.add(new MyTransition(tran.getName(),tran.getSourceName(), target2.getName()));
					 }
					 else if(target.statetype.equalsIgnoreCase("finalstate") && target.parentstate != null) {
						 MyState parent = findStateByName(allunflatstates, target.parentstate);
						 for (MyTransition t2: parent.getOutgoingTransitions()) {
							 MyState target3 = findStateByName(allunflatstates, t2.targetName);
							 if (target3.statetype.equalsIgnoreCase("finalstate") && target3.parentstate != null) {
								 MyState parent2 = findStateByName(allunflatstates, target3.parentstate);
								 for (MyTransition t3: parent2.getOutgoingTransitions()) {
									 transNewState.add(new MyTransition(t2.getName(),tran.getSourceName(), t3.targetName));
								 }
							 }
							 else 
								 transNewState.add(new MyTransition(t2.getName(),tran.getSourceName(), t2.targetName));
						 }
						 
					 }
					 
					 else
						 transNewState.add(tran);
				 }
				 newState.setOutgoingTransitions(transNewState);
				 flatstates.add(newState);

			 }
			 
			// flatstates.add(newState);

		 }
		/*
		 * ArrayList<MyState> newstates = new ArrayList<>();
		 * 
		 * for (MyTransition tran: oldtransitions ) { MyState src =
		 * findStateByName(allunflatstates, tran.getSourceName()); if (src.iscomposite
		 * == false) newstates.add(src); else {
		 * 
		 * }
		 * 
		 * 
		 * }
		 */
		 
		
		 return flatstates;
	}
	
	public static MyState findInitialOfComposite(ArrayList<MyState> states, MyState target) {
		
		for (MyState st:states ) {
			if (st.parentstate != null && st.statetype.equalsIgnoreCase("pseudostate") && st.parentstate.equalsIgnoreCase(target.getName())) {
				
				for (MyTransition trans: st.outgoingTransitions) {
					return findStateByName( states,  trans.getTargetName());
				}
			}
		}
		return null;
	}
	
	public static MyState findStateByName(ArrayList<MyState> states, String name) {
		for (MyState st: states) {
			if (st.getName().equalsIgnoreCase(name)) {
				return st;
			}
		}
		return null;
	}
	
	public static Vertex findVertexByName(ArrayList<Vertex> vertices, String name) {
		for (Vertex st: vertices) {
			if (st.getName().equalsIgnoreCase(name)) {
				return st;
			}
		}
		return null;
	}
	
	public static Model convertToModel(ArrayList<MyState> states) {
		Model model_ = UMLFactory.eINSTANCE.createModel();
		 model_.setName("MyModel2");
		  Package p =  model_.createNestedPackage("MyPackage3");//UMLFactory.eINSTANCE.createPackage(); 
		  PackageableElement pe =p.createPackagedElement("SM", UMLPackage.Literals.STATE_MACHINE);
		  pe.setName("MySM3");
		  StateMachine sm = (StateMachine) pe;
		  Region r = sm.createRegion("region1");
		  ArrayList<Vertex> allVertices = new ArrayList<>();
		  for (MyState state: states) {
			  Vertex v = null;
			  if (state.statetype.equalsIgnoreCase("state")) {
				  v = r.createSubvertex(state.getName(), UMLPackage.Literals.STATE);
				  
			  }
			  else if (state.statetype.equalsIgnoreCase("finalstate"))
				  v = r.createSubvertex(state.getName(), UMLPackage.Literals.FINAL_STATE);
			  else
				  v = r.createSubvertex(state.getName(), UMLPackage.Literals.PSEUDOSTATE);
			  allVertices.add(v);
		  }
		  
		  for (MyState state: states) {
			  
			  for (MyTransition tran: state.getOutgoingTransitions() ) {
				  Transition t = r.createTransition(tran.getName());
				  t.setSource(findVertexByName(allVertices,tran.getSourceName()));
				  t.setTarget(findVertexByName(allVertices,tran.getTargetName()));
			  }
		  }
		  
		  
		  return model_;
	}
	
}
