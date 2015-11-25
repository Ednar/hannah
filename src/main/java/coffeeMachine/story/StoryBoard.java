package coffeeMachine.story;

import coffeeMachine.customerAgent.behaviour.StoryBoardBehaviour;
import jade.core.Agent;

public class StoryBoard {


    public static StoryBoardBehaviour coffeeMakingHandler;

    public static void setCoffeeMakingHandler(final StoryBoardBehaviour coffeeMakingHandler) {
        StoryBoard.coffeeMakingHandler = coffeeMakingHandler;
    }



    public static Agent buyer;


    public static void setBuyer(final Agent buyer) {
        StoryBoard.buyer = buyer;
    }

}
