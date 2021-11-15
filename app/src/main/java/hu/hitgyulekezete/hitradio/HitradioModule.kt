package hu.hitgyulekezete.hitradio

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import hu.hitgyulekezete.hitradio.model.news.NewsRepository
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import hu.hitgyulekezete.hitradio.model.programguide.api.ProgramGuideApi
import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository
import hu.hitgyulekezete.hitradio.model.programguide.repository.NetworkProgramGuideRepository
import hu.hitgyulekezete.hitradio.model.programguide.repository.ProgramGuideRepository

@Module
@InstallIn(SingletonComponent::class)
class HitradioModule {
    @Provides
    fun provideProgramGuideRepository(): ProgramGuideRepository {
        return NetworkProgramGuideRepository(
            ProgramGuideApi("https://www.hitradio.hu/api/musor_ios.php")
        )
    }

    @Provides
    fun provideNewsRepository(): NewsRepository {
        return NewsRepository()
    }

    @Provides
    fun provideProgramRepository(): ProgramRepository {
        return ProgramRepository()
    }
}