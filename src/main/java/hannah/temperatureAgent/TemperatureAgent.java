package hannah.temperatureAgent;

import hannah.utils.ConversationIds;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TemperatureAgent extends Agent {

    String w1DirPath = "/sys/bus/w1/devices/28-021562c60fff/w1_slave";

    @Override
    protected void setup() {
        System.out.println("Temperaturagent är igång");
        addBehaviour(new TickerBehaviour(this, 1_000) {
            @Override
            protected void onTick() {
                System.out.println("Temperatur kontrolleras");
                String filePath = w1DirPath;
                File file = new File(filePath);
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String output;
                    if ((output = br.readLine()) != null) {
                        int idx = output.indexOf("t=");
                        if (idx > -1) {
                            // Temp data (multiplied by 1000) in 5 chars after t=
                            float tempC = Float.parseFloat(
                                    output.substring(output.indexOf("t=") + 2));
                            // Divide by 1000 to get degrees Celsius
                            tempC /= 1000;
                            String temp = String.format("%.3f ", tempC);

                            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                            message.setConversationId(ConversationIds.TEMP);
                            message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
                            message.setSender(myAgent.getAID());
                            message.setContent(temp);
                            myAgent.send(message);
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }
}
