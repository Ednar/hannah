package coffeeMachine.customerAgent.behaviour;

import coffeeMachine.story.StoryBoard;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class CoffeeMakingHandler extends Behaviour {

    private final String coffeeMachine;

    private AID machine;



    public CoffeeMakingHandler(final String coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
        machine = new AID(this.coffeeMachine, AID.ISLOCALNAME);

    }

    @Override
    public void action() {
        switch (StoryBoard.step) {
            case TURN_ON:

            //    StoryTellingUtils.dramaticPause();
           //     BashShellPrinter.clear();
                StoryBoard.setBuyer(myAgent);
                StoryBoard.turnOnCoffee();
                break;
            case WAIT_FOR_TURN_ON_ACCEPT:
                System.out.println("CASE: WAIT_FOR_TURN_ON_ACCEPT");

                ACLMessage requestMessage = myAgent.blockingReceive();
                if (requestMessage.getPerformative() == ACLMessage.AGREE) {
                    System.out.println("Tar emot meddelande från Dave: " + requestMessage.getPerformative());
                    StoryBoard.step = StoryBoard.Step.WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE;
                }

                // receiveMessage(ACLMessage.AGREE, Step.WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE, "Tar emot meddelande om att maskinen är på");
                break;
            case WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE:
                System.out.println("CASE: WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE");

                requestMessage = myAgent.blockingReceive();
                System.out.println("*** TAR EMOT ETT JÄVLA MEDDELANDE. ÄR: " + requestMessage.getPerformative() + " ska vara " + ACLMessage.AGREE);
                if (requestMessage.getPerformative() == ACLMessage.REQUEST) {
                    System.out.println("Tar emot meddelande från Dave: " + requestMessage.getPerformative());
                    StoryBoard.step = StoryBoard.Step.TURNING_ESPRESSO_KNOB;
                }


               // receiveMessage(ACLMessage.REQUEST, Step.TURNING_ESPRESSO_KNOB, "Tog emot en request. Väntar på att Dave ska be mig trycka på espresso-knappen");
                break;
            case TURNING_ESPRESSO_KNOB:
                System.out.println("CASE: TURNING_ESPRESSO_KNOB");
                sendMessage(ACLMessage.AGREE);
                System.out.println("Trycker på espresso-knappen (skickar en agree)");
                StoryBoard.step = StoryBoard.Step.TURNING_STEAM_KNOB;
                break;
            case TURNING_STEAM_KNOB:
                System.out.println("CASE: TURNING_STEAM_KNOB");
                receiveMessage(ACLMessage.REQUEST, StoryBoard.Step.WAIT_FOR_DAVE_TO_MAKE_STEAM, "Trycker på espresso-knappen");
                break;
        }

    }

    private void sendMessage(int performative) {
        ACLMessage message = new ACLMessage(performative);
        message.addReceiver(machine);
        message.setSender(myAgent.getAID());
        myAgent.send(message);
        System.out.println("Skickar meddelande till " + machine + " " + performative);
    }

    private void receiveMessage(int performative, StoryBoard.Step step, String message) {

        ACLMessage requestMessage = myAgent.blockingReceive();
        if (requestMessage.getPerformative() == performative) {
            System.out.println("Tar emot meddelande från Dave: " + requestMessage.getPerformative());
            System.out.println(message);
            StoryBoard.step = step;
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
