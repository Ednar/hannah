package coffeeMachine.customerAgent.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class CoffeeMakingHandler extends Behaviour {

    private final String desiredCoffee;
    private final String coffeeMachine;



    private Step step = Step.TURN_ON;
    private ACLMessage message;

    private enum Step {
        TURN_ON,
        HEAT_AND_PRESSURE_READY,
        TURN_OFF
    }


    public CoffeeMakingHandler(final String desiredCoffee, final String coffeeMachine) {
        this.desiredCoffee = desiredCoffee;
        this.coffeeMachine = coffeeMachine;
    }

    @Override
    public void action() {
                AID coffeeMachine = new AID(this.coffeeMachine, AID.ISLOCALNAME);

                message = new ACLMessage(ACLMessage.REQUEST);
                message.addReceiver(coffeeMachine);
                message.setSender(myAgent.getAID());
                myAgent.send(message);
                System.out.println("Message sent to machine");

                step = Step.HEAT_AND_PRESSURE_READY;
    }

    @Override
    public boolean done() {
        return step == Step.HEAT_AND_PRESSURE_READY;
    }


}
