package com.nosaa.hospital.domain;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class Address {
    
    private String street;

    private String suburb;

    private String state;

    private String postCode;

    public Address() {
    }

    public Address(String street, String suburb, String state, String postCode) {
        this.street = street;
        this.suburb = suburb;
        this.state = state;
        this.postCode = postCode;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuburb() {
        return this.suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, suburb, state, postCode);
    }

    @Override
    public String toString() {
        return DomainConstants.ADDRESS_STREET_TAG + getStreet() +", "
             + DomainConstants.ADDRESS_SUBURB_TAG + getSuburb() + ", "
             + DomainConstants.ADDRESS_STATE_TAG + getState() +", "
             + DomainConstants.ADDRESS_POST_CODE_TAG + getPostCode();
    }

    public Address fromString(String addressStr){
        Address address = new Address();
        address.setStreet(StringUtils.substringBetween(addressStr, DomainConstants.ADDRESS_STREET_TAG, ", "));
        address.setSuburb(StringUtils.substringBetween(addressStr, DomainConstants.ADDRESS_SUBURB_TAG, ", "));
        address.setState(StringUtils.substringBetween(addressStr, DomainConstants.ADDRESS_STATE_TAG, ", "));
        address.setPostCode(StringUtils.substringBetween(addressStr, DomainConstants.ADDRESS_POST_CODE_TAG));
        return address;
    }
    

}
