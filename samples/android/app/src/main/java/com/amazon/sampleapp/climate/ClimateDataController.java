package com.amazon.sampleapp.climate;

import com.amazon.sampleapp.climate.bean.ClimateData;
import com.amazon.sampleapp.climate.bean.Modification;

import io.reactivex.Observable;

public interface ClimateDataController {
    Observable<Modification<ClimateData>> getModificationRequest();
}
