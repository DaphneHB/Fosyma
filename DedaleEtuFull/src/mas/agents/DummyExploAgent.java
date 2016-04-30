package mas.agents;


import mas.abstractAgent;
import mas.behaviours.ExploreBehaviour;
import strategies.Strategy;
import strategies.exploring.ShorterWayStrategy;
import Objects.Carte;
import env.Environment;


public class DummyExploAgent extends abstractAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784844593772918359L;

	// useless?
	private Carte maCarte;


	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	protected void setup(){

		super.setup();

		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args[0]!=null){

			deployAgent((Environment) args[0]);

		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		
		maCarte = new Carte();
		
		//Add the behaviours
		//addBehaviour(new SayHello(this));
		Strategy strat = new ShorterWayStrategy();
		ExploreBehaviour explorBeh = new ExploreBehaviour(this,maCarte,strat);
		System.out.println(maCarte.toStringKnowledge());
		addBehaviour(explorBeh);
		
		System.out.println("the agent "+this.getLocalName()+ " is started");
	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}
