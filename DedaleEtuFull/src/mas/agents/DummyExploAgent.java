package mas.agents;

import Objects.Service;


public class DummyExploAgent extends MyAgent{

	private static final long serialVersionUID = 1L;
	
	public DummyExploAgent(){
		super();
		this.monService = Service.explorer;
	}
}
