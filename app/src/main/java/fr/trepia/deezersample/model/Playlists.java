package fr.trepia.deezersample.model;

import java.util.List;

public class Playlists extends BaseModel {

    private List<PlaylistData> data;

    public List<PlaylistData> getPlaylists() {
        return data;
    }

    public class PlaylistData {

        private long id;
        private String title;
        private long duration;
        private int nb_tracks;
        private String picture;
        private String picture_small;
        private String picture_medium;
        private String picture_big;
        private String checksum;
        private Creator creator;

        public long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public long getDuration() {
            return duration;
        }

        public int getNb_tracks() {
            return nb_tracks;
        }

        public String getPicture() {
            return picture;
        }

        public String getPicture_small() {
            return picture_small;
        }

        public String getPicture_medium() {
            return picture_medium;
        }

        public String getPicture_big() {
            return picture_big;
        }

        public String getChecksum() {
            return checksum;
        }

        public Creator getCreator() {
            return creator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PlaylistData that = (PlaylistData) o;

            if (id != that.id) return false;
            return checksum.equals(that.checksum);

        }

        @Override
        public int hashCode() {
            int result = (int) (id ^ (id >>> 32));
            result = 31 * result + checksum.hashCode();
            return result;
        }
    }
}
