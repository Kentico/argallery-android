package com.sumera.argallery.data.store.remote.model;

import com.kenticocloud.delivery_core.elements.AssetsElement;
import com.kenticocloud.delivery_core.elements.NumberElement;
import com.kenticocloud.delivery_core.elements.TextElement;
import com.kenticocloud.delivery_core.elements.models.AssetModel;
import com.kenticocloud.delivery_core.models.item.ContentItem;
import com.kenticocloud.delivery_core.models.item.ElementMapping;

public final class PictureModel extends ContentItem {

    public static final String TYPE = "picture";

    @ElementMapping("title")
    public TextElement title;

    @ElementMapping("author")
    public TextElement author;

    @ElementMapping("description")
    public TextElement description;

    @ElementMapping("picture")
    public AssetsElement picture;

    @ElementMapping("price")
    public NumberElement price;

    public String getId() {
        return getSystem().getId();
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getAuthor() {
        return author.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    public String getTeaserImageUrl() {
        AssetModel[] assets = this.picture.getValue();
        if (assets == null) {
            return null;
        }

        if (assets.length == 0) {
            return null;
        }

        return assets[0].url;
    }

    public int getPrice() {
        return price.getValue().intValue();
    }
}