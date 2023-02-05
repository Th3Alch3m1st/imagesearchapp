package com.payback.imagesearch.mapper

import com.payback.core.network.Resource
import com.payback.imagesearch.data.api.ImageSearchApiTest
import com.payback.imagesearch.data.dto.ImageResponse
import com.payback.imagesearch.utils.TestUtils
import com.payback.testutil.shouldEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Rafiqul Hasan
 */
@RunWith(JUnit4::class)
class ImageResponseToImageUIModelMapperTest {
	private lateinit var sutMapper: ImageResponseToImageUIModelMapper

	private lateinit var imageResponse:ImageResponse
	@Before
	fun setUp() {
		sutMapper = ImageResponseToImageUIModelMapper()
	}

	@Test
	fun `map ImageResponse should return Correct ImageUIModel`(){
		//Act
		imageResponse = (TestUtils.getImageSearchPagingData(ImageSearchApiTest.PAGE_1_DATA) as Resource.Success).data.hits?.get(0)!!
		val uiModel = sutMapper.map(imageResponse)

		//Verify
		uiModel.id shouldEqual imageResponse.id
		uiModel.thumbnail shouldEqual imageResponse.previewURL
		uiModel.imageUrl shouldEqual imageResponse.largeImageURL
		uiModel.sizeRatio shouldEqual 1.5
		uiModel.tags.joinToString(", ") shouldEqual imageResponse.tags
		uiModel.numberOfLikes shouldEqual imageResponse.likes
		uiModel.numberOfDownloads shouldEqual imageResponse.downloads
		uiModel.numberOfComments shouldEqual imageResponse.comments
		uiModel.username shouldEqual imageResponse.user
		uiModel.userImage shouldEqual imageResponse.userImageURL
	}

	@Test
	fun `map ImageResponse with previewWidth zero should ImageUIModel ratio 0`(){
		//Arrange
		val image = ImageResponse(imageWidth = 100, imageHeight = 0)

		//imageResponseCopy.imageHeight shouldEqual 0
		//Act
		val uiModel = sutMapper.map(image)

		//Verify
		uiModel.sizeRatio shouldEqual 0.0
	}

	@Test
	fun `map ImageResponse with null tag should return empty list`(){
		//Arrange
		val image = ImageResponse(tags = null)

		//Act
		val uiModel = sutMapper.map(image)

		//Verify
		uiModel.tags shouldEqual listOf()
	}

	@Test
	fun `map ImageResponse with empty tag should return empty list`(){
		//Arrange
		val image = ImageResponse(tags = "")

		//Act
		val uiModel = sutMapper.map(image)

		//Verify
		uiModel.tags shouldEqual listOf()
	}
}