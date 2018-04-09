package com.slq.tokfm.fragments;

import java.util.List;

interface FragmentWithProgress<T> {
    void progress(List<T> items);
}
