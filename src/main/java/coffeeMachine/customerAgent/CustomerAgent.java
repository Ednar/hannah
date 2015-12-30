package coffeeMachine.customerAgent;

import coffeeMachine.customerAgent.behaviour.StoryBoardBehaviour;
import coffeeMachine.customerAgent.behaviour.StoryTellingBehaviour;
import jade.core.Agent;

/**
 * En kund som köper kaffe av kaffemaskinen.
 *
 * Tar emot namnet på kaffemaskinen som argument när den startas.
 */
public class CustomerAgent extends Agent {

    private String agentName;
    private String sellerAgent;

    @Override
    protected void setup() {
        // Eftersom JADE har en egen runtime kan den vanliga konstruktorn inte användas för att ta emot argument
        // då alla objekt måste instantierade innan JADE kopplas på.
        // Därför finns en egen metod för att ta emot argument.
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