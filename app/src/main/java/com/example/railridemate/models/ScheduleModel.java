package com.example.railridemate.models;

public class ScheduleModel {
        private String route;
        private String train;
        private String in;
        private String out;
        private String at1;
        private String at2;

        public ScheduleModel(String route, String train, String in, String out, String at1, String at2) {
            this.route = route;
            this.train = train;
            this.in = in;
            this.out = out;
            this.at1 = at1;
            this.at2 = at2;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getTrain() {
            return train;
        }

        public void setTrain(String train) {
            this.train = train;
        }

        public String getIn() {
            return in;
        }

        public void setIn(String in) {
            this.in = in;
        }

        public String getOut() {
            return out;
        }

        public void setOut(String out) {
            this.out = out;
        }

        public String getAt1() {
            return at1;
        }

        public void setAt1(String at1) {
            this.at1 = at1;
        }

        public String getAt2() {
            return at2;
        }

        public void setAt2(String at2) {
            this.at2 = at2;
        }
    }

