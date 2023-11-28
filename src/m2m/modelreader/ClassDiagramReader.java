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
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Profile;
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

import m2m.pojo.MyAction;
import m2m.pojo.ModelClass;
import m2m.pojo.MyStateMachine;
import m2m.pojo.StereotypeAttribute;


/**
 * A class that reads UML class diagram in XMI format and prints its elements.
 * 
 * @author Nigar Azhar Butt
 * @version 1.0
 */
public class ClassDiagramReader {

	public static ArrayList<MyState> allmystates = new ArrayList<>();
	public static ArrayList<MyTransition> allmytransition = new ArrayList<>();
	public static String className;
	
	//String umlFilePath = "Examples/psm.uml";
	public static ModelClass getClassesFromFile(String umlFilePath ){
		 //allmystates = new ArrayList<>();
		 //allmytransition = new ArrayList<>();
		ModelClass Game = new ModelClass();
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
		
		EList<Profile> profiles = ((Package) objModel).getAllAppliedProfiles();
		for (Profile pf : profiles){
			System.out.print("PRofile   ");
			System.out.println(pf.getName());
			
		}
		Stereotype myStereotype =((Package) objModel).getAppliedProfile("PlatformGameTestingProfile").getOwnedStereotype("Game");
		System.out.println(myStereotype.getLabel());
		Class umlgameclass = getGameClass(sourcePackagedElements);
		if (umlgameclass != null) {
			Game.setName(umlgameclass.getName());
			Game.setStereotype(umlgameclass.getAppliedStereotypes().get(0).getLabel());
			EList<Property> attributes = umlgameclass.getAllAttributes();				
			System.out.println("Attributes: ");
			if(!attributes.isEmpty()){
				for (Property attr: attributes)
				{
					System.out.println(attr.getName());
				}
			}
			//umlgameclass.gett
		    //StereotypeApplication myStereotypeApplication = umlgameclass.getStereotypeApplication(myStereotype);
			//String myAttribute1Value = (String) umlgameclass.getValue(myStereotype, "score");
			int myAttribute2Value = (int) umlgameclass.getValue(myStereotype, "score");
			int time = (int) umlgameclass.getValue(myStereotype, "timeelapsed");
			System.out.println(myAttribute2Value);
			System.out.println(umlgameclass.getValue(myStereotype, "timeelapsed"));
			StereotypeAttribute score = new StereotypeAttribute( "score", "numeric", "",(float)myAttribute2Value );
			StereotypeAttribute timeelapsed = new  StereotypeAttribute( "timeelapsed", "numeric", "",(float)time );
			ArrayList<StereotypeAttribute> atts = new ArrayList<StereotypeAttribute>();

	        atts.add(score);
	        atts.add(timeelapsed);
	        Game.setStereotype_attributes(atts);
	        
	        
	        
	        
	        
	        System.out.println("Trying to get all assets");
	        EList<Property> assets = umlgameclass.getAllAttributes();				
			System.out.println("Assets: ");
			ArrayList<ModelClass> gameassets = null;
			if(!assets.isEmpty()){
				gameassets =  new ArrayList<ModelClass>();

				for (Property attr: assets)
				{
					System.out.print(attr.getType().getName());
					Class asset = getClassbyName(sourcePackagedElements, attr.getType().getName());
					System.out.print("  "+asset.getAppliedStereotypes().get(0).getLabel());
					
					
					System.out.println();
					String stero = asset.getAppliedStereotypes().get(0).getLabel();
					ModelClass newasset = new ModelClass();
					newasset.setName(attr.getName());
					newasset.setStereotype(stero);
					newasset.setStereotype_attributes(setAllStereotypeAttributes(asset));
					
					System.out.print("multiplicity ");
				    System.out.println(attr.isMultivalued());
				    if (attr.isMultivalued()) {
				    	newasset.setMultiplicity("*");
				    }
				    else {

				    	newasset.setMultiplicity("1");
				    }
					//ValueSpecification valueSpecification = multiplicityElement.getUpperValue();//.getValue();

//					if (valueSpecification instanceof LiteralInteger) {
//					    int multiplicity = ((LiteralInteger) valueSpecification).getValue();
//					    System.out.print("multiplicity ");
//					    System.out.println(multiplicity);
//					}
					//attr.getAssociation()
					gameassets.add(newasset);
					if (stero.equalsIgnoreCase("Avatar")) {
						System.out.println("I am avatar");
						System.out.println("Getting All operations");
						ArrayList<MyAction> actions = new ArrayList<MyAction>();
				        EList<Operation> allactions = asset.getAllOperations();
				        for(Operation act: allactions) {
				        	System.out.println("Action: "+act.getName());
//				        	if (act.getAppliedStereotypes()!=null)
//				        		System.out.println(act.getAppliedStereotypes().get(0).getLabel());
//				        	else {
//				        		
//				        	}
				        	actions.add(new MyAction(act.getName(),"-",4));
				        	
				        }
				        Game.setActions(actions);
					}
				}
			}
			else {
				System.out.println("could not find any assets ;(");
			}

			Game.setAssets(gameassets);
		}
		else {
			System.out.println("Game Class Not found");
		}
		MyStateMachine sm = new MyStateMachine();
		sm.setParent(Game.getName());
		
		ArrayList<MyState> allmystates =StateMachineReader.getStateMachineStatesFromFile(umlFilePath);
		sm.setAllmystates(allmystates);
		Game.setStatemachine(sm);

//		for (PackageableElement element : sourcePackagedElements){
//			//for nested package
//			if(element.eClass() == UMLPackage.Literals.PACKAGE){
//				org.eclipse.uml2.uml.Package nestedPackage = (org.eclipse.uml2.uml.Package) element;
//				EList<PackageableElement> nestedPackagedElements = nestedPackage.getPackagedElements();
//				for (PackageableElement nestedElement : nestedPackagedElements){
//					System.out.println("\n\nMainOverview\n\n");
//					printModelDetails(nestedElement);
//				}
//			}
//			else
//				System.out.println("\n\nMainOverview\n\n");
//				//printModelDetails(element);
//				
//				
//				
//				
//				
//				
//				
//				
//				
//		}
		
		return Game;
	}
	
	private static ArrayList<StereotypeAttribute> setAllStereotypeAttributes(Class asset){
		System.out.println("\n----------Looking for a Relevent Stero attr and values----------");
		
		
		ArrayList<StereotypeAttribute> stereoattr = new ArrayList<StereotypeAttribute>();
		
		Stereotype stereo = asset.getAppliedStereotypes().get(0);
		
		String sterename = stereo.getLabel();
		int x = (int) asset.getValue(stereo, "xCoordinate");
		int y = (int) asset.getValue(stereo, "yCordinate");
		String filelocation = (String) asset.getValue(stereo, "fileLocation");
		//int time = (int) umlgameclass.getValue(myStereotype, "timeelapsed");
		//StereotypeAttribute score = new StereotypeAttribute( "score", "numeric", "",(float)myAttribute2Value );
		
		stereoattr.add(new StereotypeAttribute("xCoordinate","numeric", "",(float)x ));
		stereoattr.add(new StereotypeAttribute("yCoordinate","numeric", "",(float)y ));
		stereoattr.add(new StereotypeAttribute("fileLocation","string", "", -1000 ));
		
		if (sterename.equalsIgnoreCase("Avatar")) {
			int lifecount = (int) asset.getValue(stereo, "lifecount");
			int direction = (int) asset.getValue(stereo, "direction");
			stereoattr.add(new StereotypeAttribute("lifecount","numeric", "",(float)lifecount ));
			stereoattr.add(new StereotypeAttribute("direction","numeric", "",(float)direction ));
		}
		else if (sterename.equalsIgnoreCase("DangerousObstacle")) {
			//No special parameters for Dangerous Obstacles
		}
		
		return stereoattr;
		
	}
	
	
	private static Class getClassbyName(EList<PackageableElement> sourcePackagedElements, String findname) {
		System.out.println("\n----------Looking for a Game Attribute----------");
		
		for (PackageableElement element : sourcePackagedElements){
			if (element.eClass() == UMLPackage.Literals.CLASS)
			{
				Class clas = (Class)element;
				
				//get class name
				String clasName = clas.getName();
				
				String stereo = clas.getAppliedStereotypes().get(0).getLabel();
				//System.out.println("Class Name: "+clasName);

				//get class attributes
				if (clasName.equalsIgnoreCase(findname)) {
					System.out.println("Class Name: "+clasName);
					System.out.println("Stereotype Name: "+stereo);
					//className = clasName;
					return clas;
				}
				
			}
			
		}
		System.out.println(findname+ " Class is not present in main package element. It is either too nested or hidden.");
		return null;
	}
	
	private static Class getGameClass(EList<PackageableElement> sourcePackagedElements) {
		for (PackageableElement element : sourcePackagedElements){
			if (element.eClass() == UMLPackage.Literals.CLASS)
			{
				System.out.println("\n----------Class Details----------");
				Class clas = (Class)element;
				
				//get class name
				String clasName = clas.getName();
				System.out.println("Class Name: "+clasName);
				className = clasName;
				String stereo = clas.getAppliedStereotypes().get(0).getLabel();
				System.out.println("Stereotype Name: "+stereo);
				//get class attributes
				if (stereo.equalsIgnoreCase("Game")) {
					return clas;
				}
				
			}
			
		}
		System.out.println("<<Game>> Class is not present in main package element. It is either too nested or hidden.");
		return null;
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
			
			System.out.println("Stereotype Name: "+clas.getAppliedStereotypes().get(0).getLabel());
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
			System.out.println("here for standalone state machine");
			readSM( (StateMachine) element);
		}
	}
	
	static public void readState(State s, State ps, String region) {
		
		 ArrayList<MyTransition> allstatetransition = new ArrayList<>();

		
		System.out.println("State: "+s.getName());
		
		System.out.println("------Outgoing Transitions------");
		EList<Transition> outgoingTransitions=s.getOutgoings();
		for (Transition transition: outgoingTransitions)
		{	
			//System.out.println("Transition Name: "+transition.getName());
			MyTransition tran = new MyTransition(transition.getName(),s.getName(), transition.getTarget().getName());
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
