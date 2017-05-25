package sma.flute_player;

import java.util.ArrayList;
import java.util.HashMap;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import sma.model.DFServices;
import sma.model.Roles;
import sma.player_agent.PlayerAgent;
import sma.vote_behaviour.CitizenSimpleSuspicionBehaviour;
import sma.vote_behaviour.CitizenSuspicionListener;
import sma.vote_behaviour.FlutePlayerScoreBehaviour;
import sma.vote_behaviour.CitizenSuspicionBehaviour;

public class FlutePlayerInitBehaviour extends OneShotBehaviour{
	private PlayerAgent agent;
	private AID receiver;

	public FlutePlayerInitBehaviour(PlayerAgent agent, AID receiver) {
		super();
		this.agent = agent;
		this.receiver = receiver;
	}


	@Override
	public void action() {
		ArrayList<Behaviour> list_behav = new ArrayList<Behaviour>();
		HashMap<String, ArrayList<Behaviour>> map_behaviour = this.agent.getMap_role_behaviours();

		CitizenSuspicionBehaviour citizenSuspicionBehaviour = new CitizenSuspicionBehaviour(this.agent);
		list_behav.add(citizenSuspicionBehaviour);

		CitizenSuspicionListener citizenSuspicionListener = new CitizenSuspicionListener(this.agent);
		list_behav.add(citizenSuspicionListener);

		CitizenSimpleSuspicionBehaviour citizenSimpleSuspicionBehaviour = new CitizenSimpleSuspicionBehaviour(this.agent);
		list_behav.add(citizenSimpleSuspicionBehaviour);
		
		FlutePlayerScoreBehaviour flutePlayerScoreBehaviour = new FlutePlayerScoreBehaviour(this.agent);
		list_behav.add(flutePlayerScoreBehaviour);
		
		FlutePlayerDeathBehaviour flutePlayerDeathBehaviour = new FlutePlayerDeathBehaviour(this.agent);
		list_behav.add(flutePlayerDeathBehaviour);
		
		this.agent.addBehaviour(citizenSimpleSuspicionBehaviour);
		this.agent.addBehaviour(citizenSuspicionBehaviour);
		this.agent.addBehaviour(citizenSuspicionListener);
		this.agent.addBehaviour(flutePlayerDeathBehaviour);
		this.agent.addBehaviour(flutePlayerScoreBehaviour);
		
		this.agent.getDeathBehaviours().add(flutePlayerDeathBehaviour.getName_behaviour());
		this.agent.getVotingBehaviours().add(citizenSuspicionBehaviour.getName_behaviour());
		
		this.agent.getVotingBehaviours().add(flutePlayerScoreBehaviour.getName_behaviour());

		
		//Handle attributes
		map_behaviour.put(Roles.FLUTE_PLAYER, list_behav);

		
		//enregirstrement
		System.out.println("[ "+this.agent.getName()+" ] REGISTER "+Roles.FLUTE_PLAYER);
		DFServices.registerPlayerAgent(Roles.FLUTE_PLAYER, this.myAgent, this.agent.getGameid());
		
		//Envoie message fin d'initialisation		
		ACLMessage messageRequest = new ACLMessage(ACLMessage.AGREE);
		messageRequest.setSender(this.agent.getAID());
		messageRequest.setConversationId("INIT_ROLE");
		messageRequest.addReceiver(this.receiver);
		this.myAgent.send(messageRequest);
	}

}
