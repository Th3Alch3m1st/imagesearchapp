package com.payback.imagesearch.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.payback.imagesearch.fakeremotesource.FakeImageSearchRemoteSourceImpl
import com.payback.testutil.uitest.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.payback.feature.imagesearch.R
import com.payback.imagesearch.testutils.ScrollToBottomAction
import org.hamcrest.CoreMatchers.not
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class ImageSearchFragmentTest {
	companion object{
		const val WAIT_TIME = 500L
	}

	@get:Rule(order = 0)
	var hiltRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val taskExecutorRule = InstantTaskExecutorRule()

	@Inject
	lateinit var fakeRemoteSource: FakeImageSearchRemoteSourceImpl

	@Before
	fun setUp() {
		hiltRule.inject()
	}

	/*
    *   Test spec
    *   name: fragment launch correctly and initial view  should be visible. No empty UI is present
    *   steps:
    *       - [Action] launch fragment
    *       - [Assert] search_view view should be visible
    *       - [Assert] rv_search_result view should be visible
    *       - [Assert] view_empty view should be invisible
    */
	@Test
	fun test_initialView_displayed() {
		//open fragment
		openFragment()

		//verifying cases
		onView(withId(R.id.search_view))
			.check(matches(isDisplayed()))
		onView(withId(R.id.rv_search_result))
			.check(matches(isDisplayed()))
		onView(withId(R.id.view_empty))
			.check(matches(not(isDisplayed())))
	}

	/*
     *   Test spec
     *   name: For A random text query, api could return empty list. UI should display empty list UI and retry button will be gone.
     *   steps:
     *       - [Action] mock error scenario by setting testError to true
     *       - [Assert] it should show empty UI
     *       - [Assert] rv_search_result should have zero item
     */
	@Test
	fun test_emptyResponse_displayed() {
		//open fragment
		openFragment()

		//creating scenario for empty response
		onView(withId(androidx.appcompat.R.id.search_src_text))
			.perform(
				ViewActions.typeText(FakeImageSearchRemoteSourceImpl.TRIGGERED_EMPTY_RESPONSE),
				ViewActions.pressImeActionButton()
			)
		Espresso.closeSoftKeyboard()

		////verifying cases
		Thread.sleep(WAIT_TIME)
		onView(withId(com.payback.core.R.id.tv_title))
			.check(matches(withText(FakeImageSearchRemoteSourceImpl.MSG_EMPTY)))
		onView(withId(com.payback.core.R.id.btn_try_again))
			.check(matches(not(isDisplayed())))
		onView(withId(R.id.rv_search_result)).check { view, noViewFoundException ->
			if (noViewFoundException != null) {
				throw noViewFoundException
			}
			val recyclerView = view as RecyclerView
			Assert.assertEquals(0, recyclerView.adapter?.itemCount)
		}
	}

	/*
     *   Test spec
     *   name: during initial loading, error may happen. UI should display error reason with retry button.
     *   steps:
     *       - [Action] mock error scenario by setting isTestError to true
     *       - [Assert] it should show error UI
     */
	@Test
	fun test_errorResponse_displayed() {
		//preparing to trigger empty response
		fakeRemoteSource.isTestError = true

		//open fragment
		openFragment()

		//verifying cases
		Thread.sleep(WAIT_TIME)
		onView(withId(com.payback.core.R.id.tv_title))
			.check(matches(isDisplayed()))
		onView(withId(com.payback.core.R.id.tv_title))
			.check(matches(withText(FakeImageSearchRemoteSourceImpl.MSG_ERROR)))
		onView(withId(com.payback.core.R.id.btn_try_again))
			.check(matches(isDisplayed()))
	}

	/*
     *   Test spec
     *   name: during initial loading, error may happen. UI should display error reason with retry button. Click on retry should fetch data again.
     *   steps:
     *       - [Action] mock error scenario by setting isTestError to true
     *       - [Assert] it should show error UI
     *       - [Action] setting isTestError to false
     *       - [Action] click on try again button
     *       - [Assert] error ui should be gone and list should be visible
     */
	@Test
	fun test_errorResponse_displayed_retry_should_fetch_data() {
		//preparing to trigger empty response
		fakeRemoteSource.isTestError = true

		//open fragment
		openFragment()

		fakeRemoteSource.isTestError = false
		onView(withId(com.payback.core.R.id.btn_try_again)).perform(click())

		//verifying cases
		Thread.sleep(WAIT_TIME)
		onView(withId(R.id.view_empty))
			.check(matches(not(isDisplayed())))
		onView(withId(R.id.rv_search_result)).check { view, noViewFoundException ->
			if (noViewFoundException != null) {
				throw noViewFoundException
			}
			val recyclerView = view as RecyclerView
			Assert.assertEquals(ImageSearchViewModel.PAGE_SIZE, recyclerView.adapter?.itemCount)
		}
	}

	/*
    *   Test spec
    *   name: Initial item loaded. UI should display 10 items in list
    *   steps:
    *       - [Action] launch fragment
    *       - [Assert] recyclerview should show 10 items
    */
	@Test
	fun test_itemLoaded_rightItemCount() {
		//open fragment
		openFragment()


		//verifying itemCount is correct
		Thread.sleep(WAIT_TIME)
		onView(withId(R.id.rv_search_result)).check { view, noViewFoundException ->
			if (noViewFoundException != null) {
				throw noViewFoundException
			}
			val recyclerView = view as RecyclerView
			Assert.assertEquals(ImageSearchViewModel.PAGE_SIZE, recyclerView.adapter?.itemCount)
		}
	}

	/*
     *   Test spec
     *   name: after initial loading, error may happen during paginate loading. UI should display error reason with retry button
     *   steps:
     *       - [Assert] should see image item
     *       - [Action] mock error scenario by setting isTestError to true
     *       - [Assert] it should show error UI
     */
	@Test
	fun test_afterInitialLoadingErrorHappen_ErrorItemDisplayed() {
		//creating scenario for retry test
		fakeRemoteSource.isRetryTest = true

		//open fragment
		openFragment()

		Thread.sleep(WAIT_TIME)
		fakeRemoteSource.isTestError = true //creating error scenario
		onView(withId(R.id.rv_search_result)).perform(ScrollToBottomAction())

		Thread.sleep(WAIT_TIME)
		//verifying cases
		onView(withId(R.id.rv_search_result))
			.check(matches(hasDescendant(withId(R.id.layout_state))))
		onView(withId(R.id.btn_paging_retry)).check(matches(isDisplayed()))
		onView(withId(R.id.tv_paging_error_msg)).check(matches(isDisplayed()))
		onView(withId(R.id.progress_bar_rv))
			.check(matches(not(isDisplayed())))
	}

	/*
	*   Test spec
	*   name: after initial loading, error may happen during pagination loading. UI should display error reason with retry button. If user click retry it should fetch data again
	*   steps:
	*       - [Assert] should see image item
	*       - [Action] mock error scenario by setting isRetryTest to true
	*       - [Assert] it should show error UI
	*       - [Action] setting isRetryTest to false
	*       - [Action] click on retry
	*       - [Assert] new list should be visible and error UI should gone
	*/
	@Test
	fun test_afterInitialLoadingErrorHappen_retryToLoadList() {
		//creating scenario for retry test
		fakeRemoteSource.isRetryTest = true

		//open fragment
		openFragment()

		Thread.sleep(WAIT_TIME)
		fakeRemoteSource.isTestError = true //creating error scenario

		onView(withId(R.id.rv_search_result)).perform((ScrollToBottomAction()))

		Thread.sleep(WAIT_TIME)
		//to visible error item
		fakeRemoteSource.isTestError = false //preparing for retry - setting isTestError to false
		onView(withId(R.id.btn_paging_retry)).perform(click())
		onView(withId(R.id.rv_search_result)).perform(ScrollToBottomAction())

		Thread.sleep(WAIT_TIME)
		//validating cases
		onView(withId(R.id.rv_search_result))
			.check(matches(not(hasDescendant(withId(R.id.btn_paging_retry)))))
		onView(withId(R.id.rv_search_result))
			.check(matches(not(hasDescendant(withId(R.id.tv_paging_error_msg)))))
		onView(withId(R.id.rv_search_result))
			.check(matches(not(hasDescendant(withId(R.id.progress_bar_rv)))))
	}

	private fun openFragment() {
		val mockNavController = Mockito.mock(NavController::class.java)
		launchFragmentInHiltContainer<ImageSearchFragment>(
			null,
			com.payback.themes.R.style.Theme_BaseAndroidApplication
		) {
			viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
				if (viewLifecycleOwner != null) {
					Navigation.setViewNavController(requireView(), mockNavController)
				}
			}
		}
	}
}