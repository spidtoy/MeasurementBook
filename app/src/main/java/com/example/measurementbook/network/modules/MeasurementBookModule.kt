package com.example.measurementbook.network.modules

import android.content.Context
import com.example.measurementbook.MeasurementBookHiltApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MeasurementModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): MeasurementBookHiltApplication {
        return app as MeasurementBookHiltApplication
    }

}