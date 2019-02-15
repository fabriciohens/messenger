package jbehave.test;

import jbehave.steps.CreateUserOkSteps;

public class CreateUserOkStoryLiveTest extends AbstractStory {

    @Override
    public String storyName() {
        return "create_user_ok.story";
    }

    @Override
    public Object stepInstance() {
        return new CreateUserOkSteps();
    }
}
