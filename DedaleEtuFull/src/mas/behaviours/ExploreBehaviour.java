package mas.behaviours;

import jade.core.behaviours.TickerBehaviour;

import java.io.IOException;
import java.util.List;

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


public class ExploreBehaviour extends TickerBehaviour{
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;
	
	private Carte carte;
	private Strategy strategy;
	
	public ExploreBehaviour (final mas.abstractAgent myagent) {
		super(myagent, 1000);
		//super(myagent);
		carte = new Carte();
		// default strategy : random
		this.strategy = new RandomStrategy();
	}
	
	public ExploreBehaviour (final mas.abstractAgent myagent, Strategy strat) {
		super(myagent, 1000);
		//super(myagent);
		carte = new Carte();
		this.strategy = strat;
	}

	public ExploreBehaviour (final mas.abstractAgent myagent, Carte carteAgent, Strategy strat) {
		super(myagent, 1000);
		//super(myagent);
		this.carte = carteAgent;
		this.strategy = strat;
	}

	@Override
	public void onTick() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		
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
			} catch(ExplorationClosedException e){
				// on ne bouge pas
				movePos = myPosition;
				// on active le behaviour de communication de la map et de recherche de tresor
				// TODO
				// on stop le behaviour courant
				this.stop();
			} catch (MeetingExpectedException e) {}
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