package com.example.railridemate.models;

public class LocationModel {
        private double latitude;
        private double longitude;
        private float direction;
//private String userId; // UserD ID associated with this location

        public LocationModel() {
            // Default constructor required for calls to DataSnapshot.getValue(LocationData.class)
        }

        public LocationModel(double latitude, double longitude,float direction) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.direction = direction;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public float getDirection() {
            return direction;
        }

        public void setDirection(float direction) {
            this.direction = direction;
        }


}
