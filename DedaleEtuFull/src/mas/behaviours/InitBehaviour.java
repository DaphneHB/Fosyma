package mas.behaviours;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;

import java.util.ArrayList;
import java.util.List;

import Objects.Service;
import strategies.Strategy;
import strategies.exploring.CommunicativeStrategy;
import strategies.exploring.ShorterWayStrategy;
import mas.agents.MyAgent;

public class InitBehaviour extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7710131400803184786L;
	private static final int SLEEPING_TIME = 1000;
	private MyAgent agent;
	private List<Behaviour> behvs = new ArrayList<Behaviour>(); 
	
	public InitBehaviour(MyAgent agent) {
		super((mas.abstractAgent)agent);
		this.agent = agent;
		// on s'enregistre sur le DF dans tous les cas
		this.agent.enregAgentDF();
		this.agent.enregServiceDF();
	}

	public InitBehaviour(MyAgent agent, List<MyBehaviour> behavs) {
		super((mas.abstractAgent)agent);
		this.agent = agent;
		// on s'enregistre sur le DF dans tous les cas
		this.agent.enregAgentDF();
		this.agent.enregServiceDF();
	}

	@Override
	public void action() {
		// on sleep qq sec SLEEPING_TIME
		try {
			Thread.sleep(SLEEPING_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.agent.everyAgents();
		//this.agent.interDF(this.agent.getService());
		this.chooseBehaviour();
		//Add the behaviours
		for(Behaviour b : this.behvs){
			this.myAgent.addBehaviour(b);
		}
	}

	@Override
	public boolean done() {
		return true;
	}
	
	private void chooseBehaviour() {
		// si c'est un explorateur il déclenche le CommunicativeWalkBehaviour
		Service service =  this.agent.getService();
		Behaviour beh;
		if (service.equals(Service.explorer)){
			Strategy strat = new CommunicativeStrategy(this.agent);
			beh = new CommunicativeWalkBehaviour(this.agent ,strat);
		}
		else { // TODO
			Strategy strat = new ShorterWayStrategy();
			beh = new ExploreBehaviour(this.agent,this.agent.getMap(),strat);
		}
		// TODO add others
		behvs.add(beh);		
	}

}
