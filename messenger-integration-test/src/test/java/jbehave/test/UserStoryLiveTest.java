package jbehave.test;

import jbehave.steps.UserSteps;

import java.util.List;

import static java.util.Arrays.asList;

public class UserStoryLiveTest extends AbstractStory {

    @Override
    public String storyName() {
        return "user.story";
    }

    @Override
    public List<Object> stepInstances() {
        return asList(new UserSteps());
    }
}
