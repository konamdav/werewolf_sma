package sma.vote_behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sma.data.ScoreFactor;
import sma.model.Roles;
import sma.model.SuspicionScore;
import sma.player_agent.PlayerAgent;

/**
 * Listener de mouvements pour les citizens
 * @author Davy
 *
 */
public class MediumSuspicionListener extends Behaviour{
	private PlayerAgent playerAgent;
	private String name_behaviour;

	private final String STATE_INIT = "INIT";
	private final String STATE_RECEIVE_INFORM = "RECEIVE_INFORM";
	private final String STATE_UPDATE = "UPDATE";

	private String step;
	private String nextStep;

	private SuspicionScore suspicionScore;
	private String player;
	private String role;

	public MediumSuspicionListener(PlayerAgent playerAgent) {
		super();
		this.playerAgent = playerAgent;
		this.name_behaviour = "MEDIUM_SUSPICION_LISTENER";

		this.suspicionScore = this.playerAgent.getSuspicionScore();

		this.step = STATE_INIT;
		this.nextStep ="";
	}

	@Override
	public void action() {

		if(step.equals(STATE_INIT))
		{
			this.player = "";
			this.role = "";
			this.nextStep = STATE_RECEIVE_INFORM;
		}
		else if(step.equals(STATE_RECEIVE_INFORM))
		{
			MessageTemplate mt = MessageTemplate.and(
					MessageTemplate.MatchPerformative(ACLMessage.INFORM),
					MessageTemplate.MatchConversationId("IS_WEREWOLF"));

			ACLMessage message = this.myAgent.receive(mt);
			if (message != null) 
			{
				this.player  = message.getContent();
				this.role = Roles.WEREWOLF;
				this.nextStep = STATE_UPDATE;

				System.err.println("LISTNER RECEIV");
			}
			else
			{
				mt = MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.INFORM),
						MessageTemplate.MatchConversationId("IS_CITIZEN"));

				message = this.myAgent.receive(mt);
				if (message != null) 
				{
					this.player  = message.getContent();
					this.role = Roles.CITIZEN;
					this.nextStep = STATE_UPDATE;

					System.err.println("LISTNER RECEIV");
				}
				else
				{

					this.nextStep = STATE_RECEIVE_INFORM;
					block(1000);
					
				}
			}		
		}
		else if(step.equals(STATE_UPDATE))
		{
			if(this.role.equals(Roles.WEREWOLF))
			{
				System.err.println("[ "+this.playerAgent.getName()+" ] is werewolf");
				this.suspicionScore.addScore(this.player, ScoreFactor.SCORE_MAX);
			}
			else
			{
				System.err.println("[ "+this.playerAgent.getName()+" ] is not a werewolf");
				this.suspicionScore.addScore(this.player, ScoreFactor.SCORE_MIN);
			}
			this.nextStep =  STATE_INIT;
		}


		if(!this.nextStep.isEmpty())
		{
			this.step = this.nextStep;
			this.nextStep ="";
		}
	}

	@Override
	public boolean done() {
		return false;
	}


	public String getName_behaviour() {
		return name_behaviour;
	}
}
