package com.alok.androidexcersise

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.alok.androidexcersise.utils.Constants
import com.alok.androidexcersise.view.MainActivity
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    private val mockWebServer = MockWebServer()
    private var okHttpClient: OkHttpClient? = null

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.alok.androidexcersise", appContext.packageName)
    }

    @Before
    fun setup() {
        mockWebServer.start(8100)
        Constants.BASE_URL = "https://localhost:8100"
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                getOkHttpClient()
            )
        )
    }

    @Test
    fun testSuccessfulResponse() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(ResponseFileReader.readResponseFromFile("facts.json"))
            }
        }
        activityRule.launchActivity(null)
        onView(withId(R.id.errorLayout))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.emptyLayout))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.noInternetLayout))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progressBar))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testFailedResponse() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().throttleBody(1024, 5, TimeUnit.SECONDS)
            }
        }

        activityRule.launchActivity(null)
        onView(withId(R.id.emptyLayout))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.errorLayout))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.noInternetLayout))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    fun getOkHttpClient(): OkHttpClient {
        return if (okHttpClient == null) {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .build()
            this.okHttpClient = okHttpClient
            okHttpClient
        } else {
            okHttpClient!!
        }
    }
}