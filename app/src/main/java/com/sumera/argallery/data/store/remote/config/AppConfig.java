package com.sumera.argallery.data.store.remote.config;

import android.support.annotation.Nullable;

import com.kenticocloud.delivery_core.models.item.TypeResolver;
import com.sumera.argallery.data.store.remote.model.PictureModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

public class AppConfig {
    public final static String KENTICO_CLOUD_PROJECT_ID = "620a29c8-8672-00b6-7084-5c282fe5f80f";

    public static List<TypeResolver<?>> getTypeResolvers() {

        // Type resolvers are responsible for creating the strongly typed object out of type
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        typeResolvers.add(new TypeResolver<>(PictureModel.TYPE, new Function<Void, PictureModel>() {
            @Nullable
            @Override
            public PictureModel apply(@Nullable Void input) {
                return new PictureModel();
            }
        }));

        return typeResolvers;
    }
}
