package coffeeMachine.customerAgent;

import coffeeMachine.customerAgent.behaviour.StoryBoardBehaviour;
import coffeeMachine.customerAgent.behaviour.StoryTellingBehaviour;
import jade.core.Agent;

public class CustomerAgent extends Agent {

    private String agentName;
    private String sellerAgent;

    @Override
    protected void setup() {
        Object[] arguments = getArguments();
        if (arguments == null || arguments.length != 1) {
            doDelete();
        } else {
            sellerAgent = String.valueOf(arguments[0]);
            agentName = getAID().getLocalName();

            StoryBoardBehaviour storyBoard = new StoryBoardBehaviour(sellerAgent);

            addBehaviour(storyBoard);
            addBehaviour(new StoryTellingBehaviour(storyBoard));
        }
    }

    @Override
    protected void takeDown() {
        System.out.println();
        System.out.println(agentName + " is terminating");
    }
}