package com.payback.imagedetails

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.payback.core.model.ImageUIModel
import com.payback.testutil.uitest.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.payback.feature.imagedetails.R
import org.junit.runner.RunWith
import org.mockito.Mockito

/**
 * Created by Rafiqul Hasan
 */

@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class ImageDetailsBottomSheetDialogTest {
	companion object {
		const val WAIT_TIME = 500L
	}

	@get:Rule
	var hiltRule = HiltAndroidRule(this)

	@Before
	fun setUp() {
		hiltRule.inject()

		imageUIModel = ImageUIModel(
			100,
			thumbnail = "https://cdn.pixabay.com/photo/2017/03/29/11/29/nepal-2184940_150.jpg",
			imageUrl = "https://pixabay.com/get/g77ff9b780f94ad2debab35905d1a0d931296856a5348f67a239937f78e9694701f2aedcc61e33968aeb32c6cb72f94a2d36ac7c24ea53458fdae270d353571fa_1280.jpg",
			sizeRatio = 1.0,
			tags = listOf("Sky", "Mountain"),
			numberOfLikes = 1100,
			numberOfDownloads = 110,
			numberOfComments = 61,
			username = "Simon",
			userImage = "https://cdn.pixabay.com/user/2019/09/16/06-10-24-33_250x250.jpeg"
		)
	}

	lateinit var imageUIModel: ImageUIModel

	@Test
	fun display_view_opened() {
		//open fragment
		openImageDetailBottomSheetFragment()


		Thread.sleep(WAIT_TIME)
		//verifying cases
		onView(withId(R.id.card_view))
			.check(matches(isDisplayed()))
		onView(withId(R.id.iv_large_image))
			.check(matches(isDisplayed()))
		onView(withId(R.id.iv_close_btn))
			.check(matches(isDisplayed()))
		onView(withId(R.id.cg_tags))
			.check(matches(isDisplayed()))
		onView(withId(R.id.chip_comment))
			.check(matches(isDisplayed()))
		onView(withId(R.id.chip_like))
			.check(matches(isDisplayed()))
	}

	@Test
	fun test_data_is_set_correctly(){
		//open fragment
		openImageDetailBottomSheetFragment()

		Thread.sleep(WAIT_TIME)

		onView(withId(R.id.chip_like))
			.check(matches(withText(imageUIModel.numberOfLikes.toString())))
		onView(withId(R.id.chip_download))
			.check(matches(withText(imageUIModel.numberOfDownloads.toString())))
		onView(withId(R.id.chip_comment))
			.check(matches(withText(imageUIModel.numberOfComments.toString())))
		onView(withId(R.id.chip_user))
			.check(matches(withText(imageUIModel.username)))
	}

	private fun openImageDetailBottomSheetFragment() {
		val bundle = Bundle().apply {
			putParcelable(
				ImageDetailsBottomSheetDialog.ARG_KEY,
				imageUIModel
			)

		}
		val mockNavController = Mockito.mock(NavController::class.java)
		launchFragmentInHiltContainer<ImageDetailsBottomSheetDialog>(
			bundle,
			com.payback.themes.R.style.BottomSheetDialog
		) {
			viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
				if (viewLifecycleOwner != null) {
					Navigation.setViewNavController(requireView(), mockNavController)
				}
			}
		}
	}
}