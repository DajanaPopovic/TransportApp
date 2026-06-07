package com.transportapp.model;

import java.time.LocalTime;

/**
 * Represents a departure (bus or train) from one station to another.
 * Contains information about type, origin, destination, departure time, duration, price, and transfer time.
 */
public class Departure {
    /**
     * The type of transport ("bus" or "train").
     */
    public String type;

    /**
     * The name of the departure station.
     */
    public String from;

    /**
     * The name of the destination city.
     */
    public String to;

    /**
     * The departure time in format "HH:mm".
     */
    public String departureTime;

    /**
     * The duration of the trip in minutes.
     */
    public int duration;

    /**
     * The price of the trip.
     */
    public int price;

    /**
     * The minimum required time for transfer (in minutes)
     */
    public int minTransferTime;

    /**
     * Default constructor required for deserialization.
     */
    public Departure(){
    }

    /**
     * Constructs a new Departure with all necessary details.
     *
     * @param type the type of transport ("bus" or "train")
     * @param from the departure station
     * @param to the destination city
     * @param departureTime the departure time in "HH:mm" format
     * @param duration the duration of the trip in minutes
     * @param price the price of the trip
     * @param minTransferTime the minimum transfer time
     */
    public Departure(String type, String from, String to, String departureTime, int duration, int price, int minTransferTime) {
       this.type=type;
       this.from=from;
       this.to=to;
       this.departureTime=departureTime;
       this.duration=duration;
       this.price=price;
       this.minTransferTime=minTransferTime;
    }

    /**
     * Returns the type of transport.
     *
     * @return the transport type
     */
    public String getType(){ return  this.type; }

    /**
     * Sets the type of transport.
     *
     * @param type the transport type ("bus" or "train")
     */
    public void setType(String type){ this.type=type; }

    /**
     * Returns the departure station.
     *
     * @return the origin station name
     */
    public String getFrom(){ return  this.from; }

    /**
     * Sets the departure station.
     *
     * @param from the origin station name
     */
    public void setFrom(String from){ this.from=from; }

    /**
     * Returns the destination city.
     *
     * @return the destination city name
     */
    public String getTo(){ return  this.to; }

    /**
     * Sets the destination city.
     *
     * @param to the destination city name
     */
    public void setTo(String to){ this.to=to; }


    /**
     * Returns the departure time as a string.
     *
     * @return the departure time in "HH:mm" format
     */
    public String getDepartureTime(){ return  this.departureTime; }

    /**
     * Sets the departure time.
     *
     * @param departureTime the departure time in "HH:mm" format
     */
    public void setDepartureTime(String departureTime){ this.departureTime=departureTime; }

    /**
     * Returns the duration of the trip in minutes.
     *
     * @return the duration in minutes
     */
    public int getDuration(){return this.duration;}

    /**
     * Sets the duration of the trip.
     *
     * @param duration the duration in minutes
     */
    public void setDuration(int duration){this.duration=duration;}


    /**
     * Returns the price of the trip.
     *
     * @return the price
     */
    public int getPrice(){return this.price;}

    /**
     * Sets the price of the trip.
     *
     * @param price the trip price
     */
    public void setPrice(int price){this.price=price;}

    /**
     * Returns the minimum transfer time after arrival.
     *
     * @return the transfer time in minutes
     */
    public int getMinTransferTime(){return this.minTransferTime;}

    /**
     * Sets the minimum transfer time after arrival.
     *
     * @param minTransferTime the transfer time in minutes
     */
    public void setMinTransferTime(int minTransferTime){this.minTransferTime=minTransferTime;}

    /**
     * Parses and returns the departure time as a {@link LocalTime} object.
     * Expects format "HH:mm".
     *
     * @return the parsed departure time
     */
    public LocalTime getParsedDepartureTime() {
        return LocalTime.parse(departureTime);
    }

    /**
     * Calculates and returns the arrival time by adding the duration to the departure time.
     *
     * @return the arrival time as {@link LocalTime}
     */
    public LocalTime getArrivalTime() {
        return getParsedDepartureTime().plusMinutes(duration);
    }

    @Override
    public String toString(){
        return this.type + " "+this.from + " "+this.to+" "+this.departureTime+" "+this.duration+" "+this.price+" "+this.minTransferTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Departure that = (Departure) o;

        return duration == that.duration &&
                price == that.price &&
                minTransferTime == that.minTransferTime &&
                type.equals(that.type) &&
                from.equals(that.from) &&
                to.equals(that.to) &&
                departureTime.equals(that.departureTime);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + departureTime.hashCode();
        result = 31 * result + duration;
        result = 31 * result + price;
        result = 31 * result + minTransferTime;
        return result;
    }


}
