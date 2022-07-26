package uk.ryanwong.gmap2ics.ui.models

import io.kotest.core.spec.style.FreeSpec
import uk.ryanwong.gmap2ics.configs.MockConfig
import uk.ryanwong.gmap2ics.data.repository.MockTimelineRepository
import uk.ryanwong.gmap2ics.ui.utils.MockResourceBundle

class MainScreenViewModelTest : FreeSpec() {

    lateinit var mainScreenViewModel: MainScreenViewModel

    private fun setupViewModel() {
        mainScreenViewModel = MainScreenViewModel(
            configFile = MockConfig(),
            timelineRepository = MockTimelineRepository(),
            resourceBundle = MockResourceBundle(),
            projectBasePath = "/default-base-path/default-sub-folder"
        )
    }

    init {
        "setExportPlaceVisit" - {
        }

        "setExportActivitySegment" - {
        }

        "setEnablePlacesApiLookup" - {
        }

        "onChangeJsonPath" - {
        }

        "onChangeICalPath" - {
        }

        "updateJsonPath" - {
        }

        "updateICalPath" - {
        }
    }
}
