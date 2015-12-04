Cappuchino
==========================

Espresso wrapper library for writing sweeter UI test.


##What's the Cappuchino like ?

Let's write a test to check if like button works as we expected in *Espresso* and *Cappuchino* way.  

**Espresso**
```java
@RunWith(AndroidJUnit4.class)
public class SampleDetailActivityTest {
  @Rule
  public ActivityTestRule<SampleDetailActivity> activityRule = new ActivityTestRule<>(
          SampleDetailActivity.class,
          true,     // initialTouchMode
          false);   // launchActivity. False so we can customize the intent per test method
    
  @Test
  public void clickLike() {
      // Setup sample intent
      Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
      Context context = instrumentation.getTargetContext();
      Intent intent = new Intent(context, SampleDetailActivity.class);
      intent.putExtra(TITLE, "Cappuchino");
      intent.putExtra(LIKE, 52);
      activityRule.launchActivity(intent);
      // Check if increment like count and button text changes to UNLIKE
      onView(withId(R.id.like_button)).perform(click());
      onView(withId(R.id.like_text)).check(matches(withText("53 likes")));
      onView(withId(R.id.like_button)).check(matches(withText(R.string.unlike)));
  }
}
```

**Cappuchino**
```java
@RunWith(AndroidJUnit4.class)
public class SampleDetailActivityTest extends Cappuchino<SampleDetailActivity> {
  @Test
  public void clickLike() {
      // Setup sample intent
      Intent intent = new Intent(getTargetContext(), SampleDetailActivity.class);
      intent.putExtra(TITLE, "Cappuchino");
      intent.putExtra(LIKE, 52);
      launchActivity(intent);
      // Check if increment like count and button text changes to UNLIKE
      perform(id(R.id.like_button)).clickView();
      expect(id(R.id.like_text)).hasText("53 likes");
      expect(id(R.id.like_button)).hasText(R.string.unlike);
  }
}
```

How was our **Cappuchino** taste ?  
Cappuchino provides you sweeter way to write Android UI test. Let's dive into them !!

##Wait for asynchronous action

Writing a test dealing with asynchronous background work is common situation in Android development.  
But it tends to be tedious work. So Cappuchino provides you sweeter way.

```java
@Test
public void checkIfProgressIsDisplayedAtFirst() {
    launchActivity(mIntent);
    // Check if progress is visible at first
    expect(id(R.id.progress)).isVisible();
    // Wait list item loading
    listIdlingTarget(R.id.list).waitFirstItemLoad();
    // Check if progress is gone after loading is done
    expect(id(R.id.progress)).isGone();
}
```

Other idling helper methods are ...
```java
viewIdlingTarget(R.id.view).waitUntilViewHasGone();      // wait until view visibility becomes GONE
listIdlingTarget(R.id.list).waitUntilItemCountOver(30);  // wait until list item count over 30
```

## Verify launched Activity

Verifying user scenario like

1. User clicks a list item to open detail screen
2. Verify if expected data is passed to detail screen

is also common situation. Let's see cappchino way !!

```java
@Test
public void openDetailScreen() {
    launchActivity(mIntent);
    // Wait until first item is loaded to perform click
    listIdlingTarget(android.R.id.list).waitFirstItemLoad();
    // Laucn DetailActivity and do assertion
    LaunchActivityAssertion
            .setTimeOut(1000)  // Wait 1 seconds for new Activity launch
            .launch(SampleDetailActivity.class, new LaunchActivityAssertion.LaunchMethod() {
                @Override
                public void launchActivity() {
                    // Difine how to launch target Activity here
                    perform(id(R.id.list)).clickItemAtPosition(0);
                }
            })
            .asserts(new LaunchActivityAssertion.ActivityAssertion() {
                @Override
                public void assertActivity(Activity activity, Intent intent) {
                    // Do assertion with launched target Activity here
                    assertEquals("Launched Activity is not SampleDetailActivity",
                            SampleDetailActivity.class, activity.getClass());
                    // Check if expected data is passed to launched activity
                    String expected_title = "item: "+0;
                    int expected_like = 0;
                    assertEquals(expected_title, intent.getStringExtra(TITLE));
                    assertEquals(expected_like, intent.getIntExtra(LIKE, -1));
                }
            });
}
```

Working implementation for sample app is placed [here](https://github.com/ishikota/Cappuchino/tree/master/app/src/androidTest/java/com/example/cappuchino).

Installation
===
Set AndroidJUnitRunner as the default test instrumentation runner  
and add dependency in your `build.gradle` file.

```groovy
android {
    defaultConfig {
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
}
dependencies {
    androidTestCompile 'jp.ikota:cappuchino:0.0.1'
}
```

License
-------

    Copyright 2015 Kota Ishimoto

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.