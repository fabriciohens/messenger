package jbehave.test;

import jbehave.steps.UserSteps;

public class UserStoryLiveTest extends AbstractStory {

    @Override
    public String storyName() {
        return "user.story";
    }

    @Override
    public Object stepInstance() {
        return new UserSteps();
    }
}
