package de.sixbits.bitspay.unit.main.activity

import android.os.Build
import android.os.Looper
import android.os.Looper.getMainLooper
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import de.sixbits.bitspay.ImageResponseFactory
import de.sixbits.bitspay.R
import de.sixbits.bitspay.main.ui.MainActivity
import de.sixbits.bitspay.main.view_model.MainViewModel
import de.sixbits.bitspay.network.model.ImageListItemModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@LooperMode(LooperMode.Mode.PAUSED)
@ExtendWith(MockitoExtension::class)
class TestMainActivity {

    private lateinit var activity: MainActivity

    private lateinit var activityController: ActivityController<MainActivity>

    @Mock
    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var searchImagesLiveData: MutableLiveData<List<ImageListItemModel>>

    @Captor
    private lateinit var searchImagesObservableCaptor: ArgumentCaptor<Observer<List<ImageListItemModel>>>

    @Mock
    private lateinit var pagerLiveData: MutableLiveData<List<ImageListItemModel>>

    @Captor
    private lateinit var pagerObservableCaptor: ArgumentCaptor<Observer<List<ImageListItemModel>>>

    @Mock
    private lateinit var loadingLiveData: MutableLiveData<Boolean>

    @Captor
    private lateinit var loadingObservableCaptor: ArgumentCaptor<Observer<Boolean>>

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        `when`(viewModel.loadingLiveData).thenReturn(loadingLiveData)
        `when`(viewModel.pagerLiveData).thenReturn(pagerLiveData)
        `when`(viewModel.searchImagesLiveData).thenReturn(searchImagesLiveData)

        activityController = Robolectric.buildActivity(MainActivity::class.java)
        activity = activityController.get()

        activityController.create()
        activity.setTestViewModel(viewModel)

        shadowOf(getMainLooper()).idle()

        activityController.start()

        verify(searchImagesLiveData).observe(
            ArgumentMatchers.any(LifecycleOwner::class.java),
            searchImagesObservableCaptor.capture()
        )
        verify(loadingLiveData).observe(
            ArgumentMatchers.any(LifecycleOwner::class.java),
            loadingObservableCaptor.capture()
        )
        verify(pagerLiveData).observe(
            ArgumentMatchers.any(LifecycleOwner::class.java),
            pagerObservableCaptor.capture()
        )
    }

    @LooperMode(LooperMode.Mode.PAUSED)
    @Test
    fun `has hidden recycler view and loading indicator on create`() {
        shadowOf(getMainLooper()).idle()
        assertEquals(
            View.GONE,
            activity.findViewById<RecyclerView>(R.id.rv_search_result).visibility
        )
        assertEquals(
            View.VISIBLE,
            activity.findViewById<CircularProgressIndicator>(R.id.pb_loading_search_result).visibility
        )
    }

    @LooperMode(LooperMode.Mode.PAUSED)
    @Test
    fun `when loading finishes the recycler should be visible and the loading indicator should go`() {
        loadingObservableCaptor.value.onChanged(false)

        shadowOf(getMainLooper()).idle()

        assertEquals(
            View.VISIBLE,
            activity.findViewById<RecyclerView>(R.id.rv_search_result).visibility
        )
        assertEquals(
            View.GONE,
            activity.findViewById<CircularProgressIndicator>(R.id.pb_loading_search_result).visibility
        )
    }
}
