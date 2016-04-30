package mas.agents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.List;

import Objects.Carte;
import Objects.Service;
import mas.abstractAgent;

public class MyAgent extends abstractAgent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8829928544353873134L;
	// exploration par defaut
	protected Service monService=Service.explorer;
	protected List<String> autresAg = new ArrayList<String>();
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
	
	public void enregDF() {
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
					this.autresAg.add(agent);
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
