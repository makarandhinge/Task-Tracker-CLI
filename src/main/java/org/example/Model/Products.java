package org.example.Model;

import java.util.List;
import java.util.Map;

public class Products {

    List<DummyModel> products;
    int total;
    int skip;
    int limit;

    public List<DummyModel> getProducts() {
        return products;
    }

    public void setProducts(List<DummyModel> products) {
        this.products = products;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
