package mas.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;

// behaviour to send msg
public class SendPosition extends TickerBehaviour implements MyBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5241477417782563306L;
	private List<String> others;
	// useless? private Strategy strat;
	
	public SendPosition(final Agent agent, List<String> data){
		super(agent,1000);
		this.others = data;
		System.out.println("others ==="+this.others);
	}

	@Override
	protected void onTick() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		ACLMessage msg=new ACLMessage(7);
		msg.setSender(this.myAgent.getAID());
		msg.setContent(this.myAgent.getLocalName()+"#"+myPosition);
		for(String receiver:others){
			msg.addReceiver(new AID(receiver,AID.ISLOCALNAME));
		}
		msg.setPerformative(ACLMessage.INFORM);
		((mas.abstractAgent)this.myAgent).sendMessage(msg);
	}

	@Override
	public boolean isWaiting() {
		return false;
	}
		
	@Override
	public void stop(){
		this.stop();
	}

	@Override
	public boolean haveToStop() {
		return false;
	}
}

