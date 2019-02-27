package jbehave.test;

import com.github.valfirst.jbehave.junit.monitoring.JUnitReportingRunner;
import jbehave.steps.RoomSteps;
import jbehave.steps.UserSteps;
import org.apache.commons.lang.StringUtils;
import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryTimeouts;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;

@RunWith(JUnitReportingRunner.class)
public class ThreadStoriesLiveTest extends JUnitStories {

    private final List<Object> stepInstances;

    public ThreadStoriesLiveTest() {
        super();
        this.stepInstances = this.getStepInstances();
        final int numOfThreads = this.stepInstances.size();

        Embedder embedder = configuredEmbedder();

        embedder.embedderControls()
                .doGenerateViewAfterStories(true)
                .doIgnoreFailureInStories(true)
                .doIgnoreFailureInView(true)
                .doVerboseFiltering(true)
                .useThreads(numOfThreads)
                .useStoryTimeouts("100secs")
                .doFailOnStoryTimeout(false);

        embedder.useTimeoutParsers(new CustomTimeoutParser());
    }

    private List<Object> getStepInstances() {
        return Arrays.asList(
                new UserSteps(),
                new RoomSteps()
        );
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), stepInstances);
    }

    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(embeddableClass))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
                        .withFormats(CONSOLE).withDefaultFormats()
                        .withFailureTrace(true).withFailureTraceCompression(true));
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**/*.story", "");
    }

    public static class CustomTimeoutParser implements StoryTimeouts.TimeoutParser {

        @Override
        public boolean isValid(String timeout) {
            return timeout.matches("(\\d+)secs");
        }

        @Override
        public long asSeconds(String timeout) {
            return Long.parseLong(StringUtils.substringBefore(timeout, "secs"));
        }

    }
}
