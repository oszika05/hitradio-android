package hu.hitgyulekezete.hitradio

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository

@Module
@InstallIn(SingletonComponent::class)
class HitradioModule {
    @Provides
    fun provideAProgramRepository(): ProgramRepository {
        return ProgramRepository()
    }
}