package coffeeMachine.customerAgent;

import coffeeMachine.customerAgent.behaviour.CoffeeMakingHandler;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

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


            addBehaviour(new TickerBehaviour(this, 10_000) {
                @Override
                protected void onTick() {
                    addBehaviour(new CoffeeMakingHandler(sellerAgent));
                }
            });
        }
    }

    @Override
    protected void takeDown() {
        System.out.println(agentName + " is terminating");
    }
}