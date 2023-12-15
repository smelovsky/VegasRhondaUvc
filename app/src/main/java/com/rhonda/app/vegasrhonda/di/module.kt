package com.rhonda.di

import android.content.Context
import com.rhonda.data.repository.api.GlobalSettingsRepositoryApi
import com.rhonda.data.repository.impl.GlobalSettingsRepositoryImpl
import com.rhonda.sdk.usb.uvc.api.UVCCameraApi
import com.rhonda.sdk.usb.uvc.impl.UVCCameraCustomImpl
import com.rhonda.app.vegasrhonda.permissions.PermissionsApi
import com.rhonda.app.vegasrhonda.permissions.PermissionsImpl
import com.rhonda.app.vegasrhonda.uvc.USBDeviceSettingsRepositoryApi
import com.rhonda.app.vegasrhonda.uvc.USBDeviceSettingsRepositoryInMemoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GlobalSettingsRepositoryModule {
    @Provides
    fun provideGlobalSettingsRepositoryApi(@ApplicationContext appContext: Context): GlobalSettingsRepositoryApi {

        return GlobalSettingsRepositoryImpl(appContext)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object PermissionsModule {
    @Provides
    fun providePermissionsApi(@ApplicationContext appContext: Context): PermissionsApi {

        return PermissionsImpl(appContext)
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
// UVC

@Module
@InstallIn(SingletonComponent::class)
object UvcApiModule {
    @Provides
    fun provideUvcApi(@ApplicationContext appContext: Context): UVCCameraApi {
        return UVCCameraCustomImpl(appContext)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object USBDeviceSettingsRepositoryModule {
    @Provides
    fun provideUSBDeviceSettingsRepositoryApi(): USBDeviceSettingsRepositoryApi {

        return USBDeviceSettingsRepositoryInMemoryImpl()
    }
}