package br.com.gcestaro.model.relationship;

public enum OrderStatus {
    OPEN, REQUESTED;

    public boolean isRequested(){
        return this.equals(REQUESTED);
    }
}
