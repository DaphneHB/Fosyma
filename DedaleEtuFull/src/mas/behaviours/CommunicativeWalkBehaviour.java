package mas.behaviours;

import jade.core.behaviours.TickerBehaviour;

import java.io.IOException;
import java.util.List;

import mas.agents.MyAgent;
import strategies.Strategy;
import strategies.exploring.RandomStrategy;
import Exceptions.ExplorationClosedException;
import Exceptions.MeetingExpectedException;
import Objects.Carte;
import env.Attribute;
import env.Couple;

/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/


public class CommunicativeWalkBehaviour extends TickerBehaviour{
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;

	private Carte carte;
	private MyAgent agent;
	private Strategy strategy;
	private String previous_position = "";
	
	public CommunicativeWalkBehaviour (MyAgent myag) {
		super((mas.abstractAgent) myag, 1000);
		this.agent = myag;
		carte = myag.getMap();
		// default strategy : random
		this.strategy = new RandomStrategy();
	}
	
	public CommunicativeWalkBehaviour (MyAgent myag, Strategy strat) {
		super((mas.abstractAgent) myag, 1000);
		this.agent = myag;
		carte = myag.getMap();		
		this.strategy = strat;
	}

	@Override
	public void onTick() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if (!myPosition.equals(previous_position)){
			// increasing the tick counter in the map
			this.carte.vueSuivante();
			this.previous_position = myPosition; // saved for the next tick
		}
		
		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			
			//// on ajoute les nouveautes a la carte
			//list of attribute associated to the currentPosition
			List<Attribute> currAttr = lobs.get(0).getRight();
			carte.addPoint(myPosition, currAttr, lobs);
			System.out.println(carte.nodeToString(myPosition,((mas.abstractAgent)this.myAgent).getLocalName()));
			//System.out.println(this.myAgent.getLocalName()+" -- list of observables: "+lobs);
			
			//Little pause to allow you to follow what is going on
			try {
				System.out.println(this.myAgent.getLocalName()+" en "+myPosition);
				System.out.println("Press a key to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// utilisation d'une strategie pour choisir le prochain pas en fct de la carte et de la position courante
			String movePos=myPosition;
			try{
				movePos = this.strategy.indexNextMove(myPosition, carte, lobs);
			} catch(MeetingExpectedException e){
				// on ne bouge pas
				movePos = myPosition;
				// on active le behaviour de communication de la map et de recherche de tresor
				// TODO
				// on stop le behaviour courant
				this.stop();
			} catch(ExplorationClosedException e){
				// on arrete d'explorer...on partage et on rencontre
			}
			//The move action (if any) should be the last action of your behaviour
			((mas.abstractAgent)this.myAgent).moveTo(movePos);
		}
		System.out.println(this.carte.toStringKnowledge());

	}
	
	
	public String serializeMap(){
		String str = this.carte.serialize();
		return str;
	}

}