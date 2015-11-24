package coffeeMachine.customerAgent.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CoffeeMakingHandler extends Behaviour {

    private final String coffeeMachine;

    private Step step = Step.TURN_ON;
    private AID machine;

    private enum Step {
        TURN_ON,
        WAIT_FOR_TURN_ON_ACCEPT,
        WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE,
        TURNING_ESPRESSO_KNOB,
        TURNING_STEAM_KNOB,
        WAIT_FOR_DAVE_TO_MAKE_STEAM,
        TURN_OFF
    }

    public CoffeeMakingHandler(final String coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
        machine = new AID(this.coffeeMachine, AID.ISLOCALNAME);
    }

    @Override
    public void action() {
        switch (step) {
            case TURN_ON:
                System.out.println("CASE: TURN_ON");
                sendMessage(ACLMessage.REQUEST);
                step = Step.WAIT_FOR_TURN_ON_ACCEPT;
                break;
            case WAIT_FOR_TURN_ON_ACCEPT:
                System.out.println("CASE: WAIT_FOR_TURN_ON_ACCEPT");
                receiveMessage(ACLMessage.AGREE, Step.WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE, "Tar emot meddelande om att maskinen är på");
                break;
            case WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE:
                System.out.println("CASE: WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE");
                receiveMessage(ACLMessage.REQUEST, Step.TURNING_ESPRESSO_KNOB, "Tog emot en request. Väntar på att Dave ska be mig trycka på espresso-knappen");
                break;
            case TURNING_ESPRESSO_KNOB:
                System.out.println("CASE: TURNING_ESPRESSO_KNOB");
                sendMessage(ACLMessage.AGREE);
                System.out.println("Trycker på espresso-knappen (skickar en agree)");
                step = Step.TURNING_STEAM_KNOB;
                break;
            case TURNING_STEAM_KNOB:
                System.out.println("CASE: TURNING_STEAM_KNOB");
                receiveMessage(ACLMessage.REQUEST, Step.WAIT_FOR_DAVE_TO_MAKE_STEAM, "Trycker på espresso-knappen");
                break;
        }

    }

    private void sendMessage(int performative) {
        ACLMessage message = new ACLMessage(performative);
        message.addReceiver(machine);
        message.setSender(myAgent.getAID());
        myAgent.send(message);
        System.out.println("Skickar meddelande till Dave " + performative);
    }

    private void receiveMessage(int performative, Step step, String message) {
        MessageTemplate template = MessageTemplate.MatchPerformative(performative);
        ACLMessage requestMessage = myAgent.blockingReceive(template);
        System.out.println("Tar emot meddelande från Dave: " + requestMessage);
        System.out.println(message);
        this.step = step;
    }

    @Override
    public boolean done() {
        return step == Step.WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE;
    }
}
