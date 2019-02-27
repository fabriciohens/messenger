package jbehave.test;

import jbehave.steps.RoomSteps;
import jbehave.steps.UserSteps;

import java.util.List;

import static java.util.Arrays.asList;

public class RoomStoryLiveTest extends AbstractStory {

    @Override
    public String storyName() {
        return "room.story";
    }

    @Override
    public List<Object> stepInstances() {
        return asList(new RoomSteps(), new UserSteps());
    }
}
