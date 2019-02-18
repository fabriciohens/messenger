package jbehave.test;

import jbehave.steps.RoomSteps;

public class RoomStoryLiveTest extends AbstractStory {

    @Override
    public String storyName() {
        return "room.story";
    }

    @Override
    public Object stepInstance() {
        return new RoomSteps();
    }
}
