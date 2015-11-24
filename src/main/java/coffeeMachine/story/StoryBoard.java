package coffeeMachine.story;

import coffeeMachine.story.chapters.Chapters;
import jade.core.Agent;

public class StoryBoard {

    public static Step step = Step.TURN_ON;

    public enum Step {
        TURN_ON,
        WAIT_FOR_TURN_ON_ACCEPT,
        WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE,
        TURNING_ESPRESSO_KNOB,
        TURNING_STEAM_KNOB,
        WAIT_FOR_DAVE_TO_MAKE_STEAM,
    }

    public static Agent buyer;

    public static void turnOnCoffee() {
        Chapters.runtChapterOne();
    }

    public static void setBuyer(final Agent buyer) {
        StoryBoard.buyer = buyer;
    }

}
