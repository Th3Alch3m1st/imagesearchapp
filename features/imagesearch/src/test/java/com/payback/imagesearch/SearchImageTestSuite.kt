package com.payback.imagesearch

import com.payback.imagesearch.data.api.ImageSearchApiTest
import com.payback.imagesearch.data.paging.ImageSearchPagingSourceTest
import com.payback.imagesearch.data.remote.ImageSearchRemoteSourceImplTest
import com.payback.imagesearch.domain.ImageSearchUseCaseImplTest
import com.payback.imagesearch.mapper.ImageResponseToImageUIModelMapperTest
import com.payback.imagesearch.presentation.ImageSearchViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

/**
 * Created by Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@SuiteClasses(
	ImageSearchApiTest::class,
	ImageSearchRemoteSourceImplTest::class,
	ImageSearchPagingSourceTest::class,
	ImageSearchUseCaseImplTest::class,
	ImageSearchViewModelTest::class,
	ImageResponseToImageUIModelMapperTest::class
)
class SearchImageTestSuite