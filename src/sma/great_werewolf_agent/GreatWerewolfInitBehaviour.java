package sma.great_werewolf_agent;

import java.util.ArrayList;
import java.util.HashMap;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import sma.model.DFServices;
import sma.model.Roles;
import sma.model.TypeIA;
import sma.player_agent.PlayerAgent;
import sma.vote_behaviour.GreatWerewolfScoreBehaviour;
import sma.vote_behaviour.WerewolfScoreBehaviour;
import sma.vote_behaviour.WerewolfSimpleSuspicionBehaviour;
import sma.vote_behaviour.WerewolfSuspicionBehaviour;
import sma.vote_behaviour.WerewolfSuspicionListener;

public class GreatWerewolfInitBehaviour extends OneShotBehaviour{
	private PlayerAgent agent;
	private AID receiver;

	public GreatWerewolfInitBehaviour(PlayerAgent agent, AID receiver) {
		super();
		this.agent = agent;
		this.receiver = receiver;
	}

	@Override
	public void action() {
		System.out.println("WerewolfInitBehaviour THIS PLAYER "+this.agent.getName());
		ArrayList<Behaviour> list_behav = new ArrayList<Behaviour>();
		HashMap<String, ArrayList<Behaviour>> map_behaviour = this.agent.getMap_role_behaviours();

		
		GreatWerewolfScoreBehaviour greatWolfScoreBehaviour = new GreatWerewolfScoreBehaviour(this.agent);
		list_behav.add(greatWolfScoreBehaviour);
		
		WerewolfScoreBehaviour werewolfScoreBehaviour = new WerewolfScoreBehaviour(this.agent);
		list_behav.add(werewolfScoreBehaviour);
		
		WerewolfSuspicionBehaviour werewolfSuspicionBehaviour = new WerewolfSuspicionBehaviour(this.agent);
		list_behav.add(werewolfSuspicionBehaviour);

		WerewolfSuspicionListener werewolfSuspicionListener = new WerewolfSuspicionListener(this.agent);
		list_behav.add(werewolfSuspicionListener);

		WerewolfSimpleSuspicionBehaviour werewolfSimpleSuspicionBehaviour = new WerewolfSimpleSuspicionBehaviour(this.agent);
		this.agent.addBehaviour(werewolfSimpleSuspicionBehaviour);

		//ScoreBehvaiour is for Vote	
		
		this.agent.addBehaviour(greatWolfScoreBehaviour);
		list_behav.add(greatWolfScoreBehaviour);
		
		this.agent.addBehaviour(werewolfScoreBehaviour);
		list_behav.add(werewolfScoreBehaviour);

		this.agent.addBehaviour(werewolfSuspicionBehaviour);
		list_behav.add(werewolfSuspicionBehaviour);

		this.agent.addBehaviour(werewolfSuspicionListener);
		list_behav.add(werewolfSuspicionListener);
		
		this.agent.getVotingBehaviours().add(werewolfScoreBehaviour.getName_behaviour());
		this.agent.getVotingBehaviours().add(werewolfSuspicionBehaviour.getName_behaviour());
		this.agent.getVotingBehaviours().add(greatWolfScoreBehaviour.getName_behaviour());
		
		this.agent.getTypeVotingBehaviours().put(werewolfSuspicionBehaviour.getName_behaviour(), TypeIA.SUSPICIOUS);
		this.agent.getTypeVotingBehaviours().put(werewolfScoreBehaviour.getName_behaviour(), TypeIA.STRATEGIC);
		this.agent.getTypeVotingBehaviours().put(greatWolfScoreBehaviour.getName_behaviour(), TypeIA.STRATEGIC);
		
		//Handle attributes
		map_behaviour.put(Roles.GREAT_WEREWOLF, list_behav);
		
		//enregirstrement
		System.out.println("[ "+this.agent.getName()+" ] REGISTER "+Roles.GREAT_WEREWOLF);
		DFServices.registerPlayerAgent(Roles.WEREWOLF, this.myAgent, this.agent.getGameid()); //simple werewolf
		DFServices.registerPlayerAgent(Roles.GREAT_WEREWOLF, this.myAgent, this.agent.getGameid());//great werewolf
		
		//Envoie message fin d'initialisation
		ACLMessage messageRequest = new ACLMessage(ACLMessage.AGREE);
		messageRequest.setSender(this.agent.getAID());
		messageRequest.setConversationId("INIT_ROLE");
		messageRequest.addReceiver(this.receiver);
		this.myAgent.send(messageRequest);	
		

	}

}
