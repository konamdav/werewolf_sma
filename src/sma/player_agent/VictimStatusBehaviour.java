package sma.player_agent;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sma.model.DFServices;
import sma.model.Status;

public class VictimStatusBehaviour extends SimpleBehaviour{
	private PlayerAgent playerAgent ;

	public VictimStatusBehaviour(PlayerAgent playerAgent) {
		super();
		this.playerAgent = playerAgent;
	}

	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchConversationId("ATTR_VICTIM_STATUS"));

		ACLMessage message = this.myAgent.receive(mt);
		if (message != null) 
		{
			System.out.println("VICTIME " + this.playerAgent.getName());
			DFServices.registerPlayerAgent(Status.VICTIM, this.playerAgent, this.playerAgent.getGameid());
		}
		else
		{
			mt = MessageTemplate.and(
			MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
			MessageTemplate.MatchConversationId("REMOVE_VICTIM_STATUS"));

			 message = this.myAgent.receive(mt);
			if (message != null) 
			{
				DFServices.deregisterPlayerAgent(Status.VICTIM, this.playerAgent, this.playerAgent.getGameid());
			}
			else
			{
				block(1000);
			}		
		}
	}

	@Override
	public boolean done() {
		return false;
	}
}
