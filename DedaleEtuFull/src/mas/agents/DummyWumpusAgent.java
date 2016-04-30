package mas.agents;

import jade.core.behaviours.SimpleBehaviour;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import mas.abstractAgent;
import env.Attribute;
import env.Environment;
import env.Couple;

public class DummyWumpusAgent extends abstractAgent{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2703609263614775545L;


	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1 set the agent attributes 
	 *	 		2 add the behaviours
	 *          
	 */
	protected void setup(){

		super.setup();

		//get the parameters given into the object[]
		final Object[] args = getArguments();
		if(args[0]!=null){
			deployWumpus((Environment) args[0]);
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}

		//Add the behaviours
		addBehaviour(new RandomWalkBehaviour(this));

		System.out.println("the  agent "+this.getLocalName()+ " is started");

	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}


	/**************************************
	 * 
	 * 
	 * 				BEHAVIOURS
	 * 
	 * 
	 **************************************/


	public class RandomWalkBehaviour extends SimpleBehaviour{
		/**
		 * When an agent choose to move
		 *  
		 */
		private static final long serialVersionUID = 9088209402507795289L;

		private boolean finished=false;
		private Environment realEnv;

		public RandomWalkBehaviour (final abstractAgent myagent) {
			//super(myagent,2000);
			super(myagent);

		}

		@Override
		public void action() {

			String myPosition=getCurrentPosition();
			if (myPosition!=""){
				List<Couple<String,List<Attribute>>> lobs=observe();
				System.out.println("lobs: "+lobs);

				try {
					System.out.println("Press a key to allow the wumpus to move to the next step "+this.myAgent.getLocalName());
					System.in.read();
				} catch (IOException e) {
					e.printStackTrace();
				}


				Random r= new Random();
				int moveId=r.nextInt(lobs.size());
				moveTo(lobs.get(moveId).getLeft());
				
			}else{
				System.out.println("Empty posit");
				System.exit(D_UNKNOWN);
			}

		}

		@Override
		public boolean done() {
			return false;
		}

	}


}
