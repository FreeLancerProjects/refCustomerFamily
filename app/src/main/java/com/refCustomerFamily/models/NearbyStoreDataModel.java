package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.List;

public class NearbyStoreDataModel implements Serializable {

    private List<NearbyModel> results;
    private ReviewModels result;

    public List<NearbyModel> getResults() {
        return results;
    }

    public ReviewModels getResult() {
        return result;
    }
}
