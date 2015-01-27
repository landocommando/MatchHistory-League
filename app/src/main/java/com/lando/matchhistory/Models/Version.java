package com.lando.matchhistory.Models;
public class Version{
    private Data n;
    public Data getVerison(){
        return n;
    }
    public static class Data {

        private String item;
        private String rune;
        private String mastery;
        private String summoner;
        private String champion;
        private String profileicon;
        private String language;

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public String getRune() {
            return rune;
        }

        public void setRune(String rune) {
            this.rune = rune;
        }

        public String getMastery() {
            return mastery;
        }

        public void setMastery(String mastery) {
            this.mastery = mastery;
        }

        public String getSummoner() {
            return summoner;
        }

        public void setSummoner(String summoner) {
            this.summoner = summoner;
        }

        public String getChampion() {
            return champion;
        }

        public void setChampion(String champion) {
            this.champion = champion;
        }

        public String getProfileicon() {
            return profileicon;
        }

        public void setProfileicon(String profileicon) {
            this.profileicon = profileicon;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }
}

