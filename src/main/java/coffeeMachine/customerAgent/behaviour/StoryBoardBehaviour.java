package coffeeMachine.customerAgent.behaviour;

import coffeeMachine.util.BashShellPrinter;
import coffeeMachine.util.MusicPlayer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import static coffeeMachine.story.StoryTellingUtils.dramaticPause;
import static coffeeMachine.story.StoryTellingUtils.slowWrite;

/**
 * Hanterar den faktiska kommunikationen med kaffemaskinen.
 *
 * Konversationen sker stegvis, i en switch för att styra flödet
 * av kaffebryggandet från början till slut.
 *
 */
public class StoryBoardBehaviour extends Behaviour {

    private final String coffeeMachine;
    public Step step = Step.INITIALIZED;

    private AID machine;

    /**
     * Steg i kommunikationen
     */
    public enum Step {
        INITIALIZED,
        TURN_ON,
        WAIT_FOR_MACHINE_TO_HEAT_AND_PRESSURIZE,
        TURNING_ESPRESSO_KNOB,
        TURNING_STEAM_KNOB,
        SKIMMING_MILK,
        WAIT_FOR_LATTE,
        DONE
    }

    /**
     * @param coffeeMachine Namnet på kaffemaskinen som används
     */
    public StoryBoardBehaviour(final String coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
        machine = new AID(this.coffeeMachine, AID.ISLOCALNAME);

        dramaticPause();
        MusicPlayer.playThemeSong(); // Spelar titelmelodi
        BashShellPrinter.clear(); // Tömmer konsolen
    }

    @Override
    public void action() {
        switch (step) {
            case INITIALIZED:
                // Väntar på att kund ska komma till maskine
                break;
            case TURN_ON:
                // Försöker slå på maskinen. Om maskinen redan är upptagen avslås förfrågan
                sendSilent(ACLMessage.REQUEST);

                int performative = receiveSilent();
                if (performative == ACLMessage.AGREE)
                    step = Step.WAIT_FOR_MACHINE_TO_HEAT_AND_PRESSURIZE;
                else {
                    doMultiplayerConflictDeath();
                }
                break;
            case WAIT_FOR_MACHINE_TO_HEAT_AND_PRESSURIZE:
                receiveSilent();
                // Väntar på att kund slår på espresso-knappen
                break;
            case TURNING_ESPRESSO_KNOB:
                sendSilent(ACLMessage.AGREE);
                step = Step.TURNING_STEAM_KNOB;
                break;
            case TURNING_STEAM_KNOB:
                receiveSilent();
                // Väntar på att kund börjar göra mjölk
                break;
            case SKIMMING_MILK:
                sendSilent(ACLMessage.AGREE);
                step = Step.WAIT_FOR_LATTE;
                break;
            case WAIT_FOR_LATTE:
                receiveSilent();
                // Väntar på att kaffe ska bli klar
                break;
            case DONE:
                myAgent.doDelete();
                break;

        }

    }

    // Om en annan kund kommer till maskinenen när den används händer detta
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
