package hannah;

import jade.core.AID;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

class SensesManager {

    private final AgentContainer containerController;

    private AID hungerAgentAID;
    private AID sleepAgentAID;

    SensesManager(final ConcurrentMoodHannah hannah) {
        // Container används för att starta nya agenter programmatiskt
        containerController = hannah.getContainerController();
    }

    void addHungerAgent() {
        try {
            String localName = "hunger";
            containerController.createNewAgent(localName, "hannah.hungerAgent.HungerAgent", null).start();
            hungerAgentAID = new AID(localName, AID.ISLOCALNAME);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    void addSleepAgent() {
        try {
            String localName = "sleep";
            containerController.createNewAgent(localName, "hannah.sleepAgent.SleepAgent", null).start();
            sleepAgentAID = new AID(localName, AID.ISLOCALNAME);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    AID getHungerAgentAID() {
        return hungerAgentAID;
    }

    AID getSleepAgentAID() {
        return sleepAgentAID;
    }
}