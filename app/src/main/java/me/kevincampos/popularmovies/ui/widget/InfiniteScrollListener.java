/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.kevincampos.popularmovies.ui.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * A scroll listener for RecyclerView to load more items as you approach the end.
 * <p>
 * Adapted from https://gist.github.com/ssinss/e06f12ef66c51252563e
 */
public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    // The minimum number of items remaining before we should loading more.
    private static final int VISIBLE_THRESHOLD = 7;

    private final LinearLayoutManager layoutManager;

    public InfiniteScrollListener(@NonNull LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy < 0) return;

        final int visibleItemCount = recyclerView.getChildCount();
        final int totalItemCount = layoutManager.getItemCount();
        final int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            onReachEnd();
        }
    }

    public abstract void onReachEnd();

}
