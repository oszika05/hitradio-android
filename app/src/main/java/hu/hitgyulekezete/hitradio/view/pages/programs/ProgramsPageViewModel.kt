package hu.hitgyulekezete.hitradio.view.pages.programs

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import javax.inject.Inject

@HiltViewModel
class ProgramsPageViewModel @Inject constructor(
    programRepository: ProgramRepository
) : ViewModel() {



}