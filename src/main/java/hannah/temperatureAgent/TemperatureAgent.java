package hannah.temperatureAgent;

import hannah.utils.ConversationIds;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;

public class TemperatureAgent extends Agent {

    String w1DirPath = "/sys/bus/w1/devices";

    @Override
    protected void setup() {
        addBehaviour(new TickerBehaviour(this, 1_000) {
            @Override
            protected void onTick() {
                File dir = new File(w1DirPath);
                File[] files = dir.listFiles(new DirectoryFileFilter());
                if (files != null) {
                    while(true) {
                        for(File file: files) {
                            System.out.print(file.getName() + ": ");
                            // Device data in w1_slave file
                            String filePath = w1DirPath + "/" + file.getName() + "/w1_slave";
                            File f = new File(filePath);
                            try(BufferedReader br = new BufferedReader(new FileReader(f))) {
                                String output;
                                while((output = br.readLine()) != null) {
                                    int idx = output.indexOf("t=");
                                    if(idx > -1) {
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
                            }
                            catch(Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                }
            }
        });
    }

    static class DirectoryFileFilter implements FileFilter
    {
        public boolean accept(File file) {
            String dirName = file.getName();
            String startOfName = dirName.substring(0, 3);
            return (file.isDirectory() && startOfName.equals("28-"));
        }
    }
}
