package coffeeMachine.customerAgent.behaviour;

import coffeeMachine.story.StoryBoard;
import coffeeMachine.story.StoryTellingUtils;
import coffeeMachine.util.BashShellPrinter;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.io.*;

public class StoryBoardBehaviour extends Behaviour {

    private final String coffeeMachine;
    public Step step = Step.INITIALIZED;

    private AID machine;

    public enum Step {
        INITIALIZED,
        TURN_ON,
        WAIT_FOR_TURN_ON_ACCEPT,
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
        System.out.println("CASE: INIT");
        StoryBoard.setCoffeeMakingHandler(this);
        StoryTellingUtils.dramaticPause();
        StoryBoard.setBuyer(myAgent);

        ((Runnable) () -> {
            Sequencer sequencer = null;
            try {
                sequencer = MidiSystem.getSequencer();
                sequencer.open();
                InputStream is = new BufferedInputStream(new FileInputStream(new File("rain.mid")));
                sequencer.setSequence(is);
                sequencer.start();
            } catch (MidiUnavailableException | IOException | InvalidMidiDataException e) {
                // Ignored lol
            }

        }).run();


        BashShellPrinter.clear();

    }

    @Override
    public void action() {
        switch (step) {
            case INITIALIZED:
                // Waiting for hero. Please stand by
                break;
            case TURN_ON:
                sendSilent(ACLMessage.REQUEST);
                step = Step.WAIT_FOR_TURN_ON_ACCEPT;
                break;
            case WAIT_FOR_TURN_ON_ACCEPT:
                int performative = receiveSilent();
                if (performative == ACLMessage.AGREE)
                    step = Step.WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE;
                break;
            case WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE:
                receiveSilent();
                break;
            case TURNING_ESPRESSO_KNOB:
                sendSilent(ACLMessage.AGREE);
                step = Step.TURNING_STEAM_KNOB;
                break;
            case TURNING_STEAM_KNOB:
                receiveSilent();
                break;
            case SKIMMING_MILK:
                sendSilent(ACLMessage.AGREE);
                step = Step.WAIT_FOR_LATTE;
                break;
            case WAIT_FOR_LATTE:
                receiveSilent();
                break;
            case DONE:
                myAgent.doDelete();
                break;

        }

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
