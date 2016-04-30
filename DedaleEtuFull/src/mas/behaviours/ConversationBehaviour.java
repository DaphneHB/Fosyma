package mas.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class ConversationBehaviour extends TickerBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2058134622078521998L;
	
	// variable en attente de reponse comptant le nombre de tick realises depuis
	private int waiting = 0;
	private String receiver;
	
	/**
	 * An agent tries to contact its friend and to give him its current position
	 * @param myagent the agent who posses the behaviour
	 *  
	 */
	public ConversationBehaviour (final mas.abstractAgent myagent) {
		super(myagent, 1000);
	}

	public ConversationBehaviour(Agent myAgent, String receiver) {
		super(myAgent, 1000);
		this.receiver = receiver;
	}

	@Override
	public void onTick() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		
		System.out.println("Current position = "+myPosition);
		ACLMessage msg=new ACLMessage(7);
		msg.setSender(this.myAgent.getAID());

//		if (myPosition!=""){
		System.out.println("Agent "+this.myAgent.getLocalName()+ " is trying to reach its friends");
		// content = agentLocalName position
		msg.setContent(this.myAgent.getLocalName()+" "+myPosition);
		
		// TODO in principal.java
		// knowing all the agent's name ?
		// in the datas in the begining?
		if (!myAgent.getLocalName().equals("Explo1")){
			msg.addReceiver(new AID("Explo1",AID.ISLOCALNAME));
		}else{
			msg.addReceiver(new AID("Explo2",AID.ISLOCALNAME));
		}

		((mas.abstractAgent)this.myAgent).sendMessage(msg);

//		}

	}

}