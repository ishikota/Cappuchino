package jp.ikota.cappuchino.assertion;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import static java.lang.String.format;
import static junit.framework.Assert.assertNotNull;


public class LaunchActivityAssertion {

    public interface LaunchMethod { void launchActivity(); }
    public interface ActivityAssertion { void assertActivity(Activity activity, Intent intent); }

    public static TimeoutHolder setTimeOut(int timeOut) {
        return new TimeoutHolder(timeOut);
    }

    public static LaunchMethodWrapper launch(Class targetClass, LaunchMethod launchMethod) {
        return new LaunchMethodWrapper(targetClass.getName(), launchMethod, 1000);
    }

    public static class TimeoutHolder {
        private final int timeOut;

        public TimeoutHolder(int timeOut) {
            this.timeOut = timeOut;
        }

        public LaunchMethodWrapper launch(Class targetClass, LaunchMethod launchMethod) {
            return new LaunchMethodWrapper(targetClass.getName(), launchMethod, timeOut);
        }
    }

    public static class LaunchMethodWrapper {

        private final String targetClassName;
        private final LaunchMethod launchMethod;
        private final int timeOut;

        private LaunchMethodWrapper(String targetClassName, LaunchMethod launchMethod, int timeOut) {
            this.targetClassName = targetClassName;
            this.launchMethod = launchMethod;
            this.timeOut = timeOut;
        }

        public void asserts(ActivityAssertion assertion) {
            // Set up an ActivityMonitor
            Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
            Instrumentation.ActivityMonitor receiverActivityMonitor =
                    instrumentation.addMonitor(targetClassName, null, false);

            // Launch target activity and asserts if it is not null
            launchMethod.launchActivity();
            Activity activity = receiverActivityMonitor.waitForActivityWithTimeout(timeOut);
            instrumentation.removeMonitor(receiverActivityMonitor);
            assertNotNull(format("%s is null", targetClassName), activity);

            // Start passed assertion if activity properly launched
            assertion.assertActivity(activity, activity.getIntent());
        }
    }

}
