package fr.trepia.deezersample.model;

import java.util.List;

public class Tracks extends BaseModel {

    private List<TracksData> data;

    public List<TracksData> getTracks() {
        return data;
    }

    public class TracksData {

        private long id;
        private String title;
        private long duration;
        private Artist artist;

        public long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public long getDuration() {
            return duration;
        }

        public Artist getArtist() {
            return artist;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TracksData that = (TracksData) o;

            return id == that.id;

        }

        @Override
        public int hashCode() {
            return (int) (id ^ (id >>> 32));
        }
    }
}
