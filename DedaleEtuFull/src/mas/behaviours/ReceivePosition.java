package mas.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import strategies.exploring.CommunicativeStrategy;

// beahviour to receive msg
public class ReceivePosition extends TickerBehaviour implements MyBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8173160599761785928L;
	private boolean finishing = false;
	private boolean waiting = false;
	private long timer = 0;
	
	public ReceivePosition(final Agent agent) {
		super(agent,1000);
	}
	
// Simple behaviour	
//	@Override
//	public void action() {
//		// matching inform performative
//		// matching accept/ack/echo performative
//		final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
//		final ACLMessage msg = this.myAgent.receive(msgTemplate);
//		if(msg!=null){
//			// verifying if its a position message or a return position msg
//			// calculating Manhattan distance to stay there or not
//			// if dist<3 : 
//			//		stop moving, 
//			//		stoping receive/send position behaviour,
//			//		launching conversation behaviour,
//			//		launching a timer
//		} else {
//			block();
//		}
//	}
//
//	@Override
//	public boolean done() {
//		return finished;
//	}
	

	// searching for a simple conversation : with only 1 agent
	@Override
	protected void onTick() {
		// si on devrait lancer une conversation
		if (finishing) {
			return;
		}
		
		// if dist<3 : 
		//		stop moving, 
		//		stoping receive/send position behaviour,
		//		launching conversation behaviour,
		//		launching a timer
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if (waiting){
			timer--;
			if (timer==0){
				waiting = false;
			}
		}		
		// matching inform performative
		// matching accept/ack/echo performative
		final MessageTemplate msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
					MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		final ACLMessage msg = this.myAgent.receive(msgTemplate);
		System.out.println(this.myAgent.getLocalName()+" is ticking son receive. MSG = "+msg);

		if(msg!=null){
			// verifying if its a position message or a return position msg
			String [] theMsg = msg.getContent().split("#");
			System.out.println("\t\t!!!!!!!!RECEIVED!!!!!!!!!!!!Message recu de "+theMsg[0]+" en "+theMsg[1]);
			// the message is a confirmation an other agent received my position and send me his
			if(theMsg[0].equals("ACK") ){
				// stopping the emission of the position?
				// lauching a conversation with the sender
				//this.myAgent.addBehaviour(new ConversationBehaviour(this.myAgent,theMsg[1]));
				System.out.println("ACK-----------> Distance OK between "+myPosition+" and "+theMsg[2]);
				System.out.println("___________________Have to launch a conversation");
			} // otherwise it is a simple sendPosition msg 
			else if(!waiting){
				System.out.println("-----------> Have to ACK");
				// dont moving, waiting for an answer to the answer
				this.waiting = true;
				this.timer = CommunicativeStrategy.WAITING_TIME;
				// sending back the position:
				ACLMessage msg1=new ACLMessage(7);
				msg1.setSender(this.myAgent.getAID());
				msg1.setContent("ACK#"+this.myAgent.getLocalName()+"#"+myPosition);
				String receiver = theMsg[0];
				msg1.addReceiver(new AID(receiver,AID.ISLOCALNAME));
				msg1.setPerformative(ACLMessage.CONFIRM);
				((mas.abstractAgent)this.myAgent).sendMessage(msg1);
				System.out.println("Sent to "+theMsg[0]+" : "+msg1);
			} else {System.out.println("WAITING");}
		} else {
			block();
		}
	}
	
	public boolean getWaitStatus(){
		return waiting;
	}

	@Override
	public boolean isWaiting() {
		return this.waiting;
	}
	
	public boolean haveToStop(){
		return finishing;
	}
	
	@Override
	public void stop(){
		this.stop();
	}

	
}

