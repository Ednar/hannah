package coffeeMachine.customerAgent;

import coffeeMachine.customerAgent.behaviour.CoffeeMakingHandler;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class CustomerAgent extends Agent {

    private String desiredCoffee;
    private String agentName;
    private String sellerAgent;

    @Override
    protected void setup() {
        Object[] arguments = getArguments();
        if (arguments == null || arguments.length != 2) {
            doDelete();
        } else {
            desiredCoffee = String.valueOf(arguments[0]);
            sellerAgent = String.valueOf(arguments[1]);
            agentName = getAID().getLocalName();
            System.out.println(agentName + " wants to buy " + desiredCoffee);


            addBehaviour(new TickerBehaviour(this, 5_000) {
                @Override
                protected void onTick() {
                    addBehaviour(new CoffeeMakingHandler(desiredCoffee, sellerAgent));
                }
            });
        }
    }

    @Override
    protected void takeDown() {
        System.out.println(agentName + " is terminating");
    }
}