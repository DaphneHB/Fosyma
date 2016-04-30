package strategies.exploring;

import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.List;

import strategies.Strategy;
import mas.agents.MyAgent;
import mas.behaviours.MyBehaviour;
import mas.behaviours.ReceivePosition;
import mas.behaviours.SendPosition;
import Exceptions.ExplorationClosedException;
import Exceptions.MeetingExpectedException;
import Objects.Carte;
import env.Attribute;
import env.Couple;

/**
 * Simple exploring strategy associated to a sending and recieving msg to exchange maps
 * or unlock the way/make mutual choices
 * @author 3200234
 *
 */
public class CommunicativeStrategy implements Strategy {
	// nb tick to wait
	public static int WAITING_TIME = 3;
	
	private String previousPosition="";
	private MyAgent theAgent;
	private String currentPos;
	private List<MyBehaviour> behavs = new ArrayList<MyBehaviour>();
	private boolean moveOk = true;
	private List<String> way = new ArrayList<String>();
	
			
	public CommunicativeStrategy(MyAgent ag) {
		this.theAgent = ag;
		// launching sendPosition Behaviour
		MyBehaviour sendPos = new SendPosition(ag, ag.getListAutresAgents());
		ag.addBehaviour((Behaviour) sendPos);
		behavs.add(sendPos);
		MyBehaviour recPos = new ReceivePosition(ag);
		ag.addBehaviour((Behaviour) recPos);
		behavs.add(recPos);
	}
	
	@Override
	public String indexNextMove(String currentPosition, Carte knowledge,
			List<Couple<String, List<Attribute>>> observations) throws ExplorationClosedException,MeetingExpectedException {
		this.currentPos = currentPosition;
		boolean meeting=false; // TODO
		// si on est sur le point de commencer un conversation behaviour
		if (meeting){
			System.out.println("===============>>> Conversation à démarrer!!!!!");
			throw new MeetingExpectedException("Rencontre entre deux agents");
		}
		// si on est en attente d'un autre agent
		if(!moveOk){
			return this.currentPos;
		}
		// TODO : si un des behaviour en cours est en attente d'un autre agent 
		for (MyBehaviour b:this.behavs){
			if (b.isWaiting()){
				moveOk = false;
				return this.currentPos;
			} else if (b.haveToStop()){
				meeting=true;
				moveOk=false;
			}
		}
		// Getting the simplest way a possible next position
		String nextPosition = knowledge.getNonVisitedNeighboor(currentPosition);
		// si toutes les positions ont déja été visitées
		if (nextPosition.equals("") || nextPosition.equals(previousPosition) || nextPosition.equals(currentPosition)){
			// on genere le plus court chemin jusquau point "tovisit"
			this.way = knowledge.getSimpleWay(currentPosition);
			// si tous les noeuds de la carte ont été visités
			if (this.way==null){
				// on en informe le behaviour by exception
				throw new ExplorationClosedException("");
			}
			// on recupere la prochaine position
			nextPosition = way.remove(0);
		}
		return nextPosition;
	}
	
	public boolean meetingToDo(){
		// stopping the sendposition and receive position behaviour
		for (MyBehaviour b:this.behavs){
			if (b.haveToStop()){
				return true;
			}
		}
		return false;
	}
	
	public String getCurrentPosition(){
		return this.currentPos;
	}
}
