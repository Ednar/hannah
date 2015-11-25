package coffeeMachine.customerAgent.behaviour;

import coffeeMachine.util.BashShellPrinter;
import coffeeMachine.util.MusicPlayer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import static coffeeMachine.story.StoryTellingUtils.dramaticPause;
import static coffeeMachine.story.StoryTellingUtils.slowWrite;

public class StoryBoardBehaviour extends Behaviour {

    private final String coffeeMachine;
    public Step step = Step.INITIALIZED;

    private AID machine;

    public enum Step {
        INITIALIZED,
        TURN_ON,
        WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE,
        TURNING_ESPRESSO_KNOB,
        TURNING_STEAM_KNOB,
        SKIMMING_MILK,
        WAIT_FOR_LATTE,
        DONE
    }

    public StoryBoardBehaviour(final String coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
        machine = new AID(this.coffeeMachine, AID.ISLOCALNAME);

        dramaticPause();
        MusicPlayer.playThemeSong();
        BashShellPrinter.clear();
    }

    @Override
    public void action() {
        switch (step) {
            case INITIALIZED:
                // Waiting for hero to arrive at the machine. Give him/her some time.
                break;
            case TURN_ON:
                // Try to turn on the machine. It can either accept if the machine is free, or refuse if it's being used.
                sendSilent(ACLMessage.REQUEST);

                int performative = receiveSilent();
                if (performative == ACLMessage.AGREE)
                    step = Step.WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE;
                else {
                    doMultiplayerConflictDeath();
                }
                break;
            case WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE:
                receiveSilent();
                // Waiting for the hero to turn the espresso knob
                break;
            case TURNING_ESPRESSO_KNOB:
                sendSilent(ACLMessage.AGREE);
                step = Step.TURNING_STEAM_KNOB;
                break;
            case TURNING_STEAM_KNOB:
                receiveSilent();
                // Waiting for the hero to start steaming milk
                break;
            case SKIMMING_MILK:
                sendSilent(ACLMessage.AGREE);
                step = Step.WAIT_FOR_LATTE;
                break;
            case WAIT_FOR_LATTE:
                receiveSilent();
                // Waiting for the hero finish the quest
                break;
            case DONE:
                myAgent.doDelete();
                break;

        }

    }

    private void doMultiplayerConflictDeath() {
        slowWrite("You see a sad creature in front of the coffee machine, its arms and hands a blur as it\n" +
                "feverishly turns knobs and presses buttons. For what eldritch purposes you do not know.\n");
        dramaticPause();
        slowWrite("Aimlessly you wander back to your desk only to perish from dehydration.");
        myAgent.doDelete();
    }

    @SuppressWarnings("Duplicates")
    public void sendMessage(int performative) {
        ACLMessage message = new ACLMessage(performative);
        message.addReceiver(machine);
        message.setSender(myAgent.getAID());
        myAgent.send(message);
        System.out.println("Skickar meddelande till " + machine.getLocalName() + " " + performative);
    }

    public void receiveMessage(int performative, String message) {
        ACLMessage requestMessage = myAgent.blockingReceive();
        if (requestMessage.getPerformative() == performative) {
            System.out.println("Tar emot meddelande från Dave: " + requestMessage.getPerformative());
            System.out.println(message);
        }
    }

    public void sendSilent(int performative) {
        ACLMessage message = new ACLMessage(performative);
        message.setConversationId(myAgent.getLocalName());
        message.addReceiver(machine);
        message.setSender(myAgent.getAID());
        myAgent.send(message);
    }

    public int receiveSilent() {
        ACLMessage requestMessage = myAgent.blockingReceive();
        return requestMessage.getPerformative();
    }

    @Override
    public boolean done() {
        return false;
    }
}
