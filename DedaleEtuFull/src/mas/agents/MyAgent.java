package mas.agents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.List;

import env.Environment;

import Objects.Carte;
import Objects.Service;
import mas.abstractAgent;
import mas.behaviours.InitBehaviour;

public class MyAgent extends abstractAgent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8829928544353873134L;
	// exploration par defaut
	protected Service monService=Service.explorer;
	protected List<String> autresAg = new ArrayList<String>();
	protected List<String> agentsService = new ArrayList<String>();
	protected List<String> data = new ArrayList<String>();
	// pour s'enregistrer sur le df et recup les autres agents
	protected boolean firstLaunch = true;
	// creation de la carte
	protected Carte maCarte = new Carte();
		
		
	public MyAgent() {
		super();
	}

	public MyAgent(String serv) {
		super();
		this.monService = Service.getService(serv);
	}

	public MyAgent(String serv, Object[] data) {
		super();
		this.monService = Service.getService(serv);
		this.getDataBack(data);
	}
	
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
		// recuperation du service de l'agent
		if (args.length>1 && args[1]!= null ) { //& args[2] instanceof String){
			if (args[1] instanceof String){
				this.monService = Service.getService((String) args[1]);
			} else{
				System.err.println("Le service demandé n'existe pas. Placement au service par défaut : "+this.monService);
			}
		} // sinon valeur par defaut
		
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
	
	public void enregAgentDF() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID()); /* getAID est l'AID de l'agent qui veut s'enregistrer*/
		ServiceDescription sd  = new ServiceDescription();
		sd.setType("agent"); /* il faut donner des noms aux services qu'on propose (ici explorer)*/
		sd.setName(this.getLocalName());
		dfd.addServices(sd);
		try {  
		      DFService.register(this, dfd );  
		}
		catch (FIPAException fe) { fe.printStackTrace(); }
		this.firstLaunch = false;
	}

	public void enregServiceDF() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID()); /* getAID est l'AID de l'agent qui veut s'enregistrer*/
		ServiceDescription sd  = new ServiceDescription();
		sd.setType(this.monService.toString()); /* il faut donner des noms aux services qu'on propose (ici explorer)*/
		sd.setName(this.getLocalName() );
		dfd.addServices(sd);
		try {  
		      DFService.register(this, dfd );  
		}
		catch (FIPAException fe) { fe.printStackTrace(); }
		this.firstLaunch = false;
	}

	public void everyAgents(){
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd  = new ServiceDescription();
		sd.setType("agent"); /* le même nom de service que celui qu'on a déclaré*/
		dfd.addServices(sd);
		            
		DFAgentDescription[] result=null;
		try {
			result = DFService.search(this, dfd);
			System.out.println("result" +result);
			for (int i=0;i<result.length;i++){
				String agent = result[i].getName().getLocalName();
				if (!agent.equals(this.getLocalName())){
					this.autresAg.add(agent);
				}
			}
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void interDF(Service serv){
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd  = new ServiceDescription();
		sd.setType(serv.toString() ); /* le même nom de service que celui qu'on a déclaré*/
		dfd.addServices(sd);
		            
		DFAgentDescription[] result=null;
		try {
			result = DFService.search(this, dfd);
			for (int i=0;i<result.length;i++){
				String agent = result[i].getName().getLocalName();
				if (!agent.equals(this.getLocalName())){
					this.agentsService.add(agent);
				}
			}
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void getDataBack(Object object) {
		this.data = new ArrayList<String>((List<String>)object);
		if (data.size()>0){
			try{
				// removing the current name of the agent from his data
				data.remove(this.getLocalName());
			} catch(Exception e){}
		}

	}

	public Service getService() {
		return this.monService;
	}

	public List<String> getListAutresAgents() {
		return this.autresAg;
	}

	public Carte getMap() {
		return this.maCarte;
	}

}
