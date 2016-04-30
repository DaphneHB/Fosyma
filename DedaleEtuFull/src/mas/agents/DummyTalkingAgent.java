package mas.agents;


import java.util.List;

import Objects.Service;
import mas.behaviours.InitBehaviour;
import env.Environment;


public class DummyTalkingAgent extends MyAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784844593772918359L;
	
	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 *          
	 *          arg[0] -> env
	 *          arg[1] -> service
	 */
	protected void setup(){

		super.setup();
		
		System.out.println("dummy talking agent");
		
		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		// arg[0] -> environnement
		if(args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0]);
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		// arg[1] -> autres agents
		//if(args.length>1 && args[1]!=null){
		//	this.getDataBack(args[1]);
		//}
		System.out.println("Env ok");
		// recuperation du service de l'agent
		if (args.length>1 && args[1]!= null ) { //& args[2] instanceof String){
			if (args[1] instanceof String){
				this.monService = Service.getService((String) args[1]);
			} else{
				System.err.println("Le service demandé n'existe pas. Placement au service par défaut : "+this.monService);
			}
		} // sinon valeur par defaut
		System.out.println("Service ok");
		
		// connaissances initiales de l'agent
		if (this.data.isEmpty()){
			// dans le dernier argument
			if (args.length>2 && args[2]!=null && args[2] instanceof List){
				this.data = ((List<String>) args[2]);
			}
			System.out.println(this.getLocalName()+" ne sais rien...pour l'instant");
		}
		else {
			System.out.println("Donnees de "+this.getLocalName()+" : "+this.data);
		}
		System.out.println("Data ok");	
		InitBehaviour init = new InitBehaviour(this);
		this.addBehaviour(init);
		System.out.println("The agent "+this.getLocalName()+ " is started (service : "+this.monService+")");
	}
	
	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){
		System.out.println("Fini");
		System.out.println(this.maCarte.toStringKnowledge());
	}	

}
