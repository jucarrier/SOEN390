package Helpers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import Models.Building;
import Models.Campus;

public class CampusBuilder {
    GoogleMap mMap;

    public CampusBuilder(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public Campus buildSGW() {
        Building hall = new Building(mMap, "Hall", "1455 Boulevard de Maisonneuve O, Montréal, QC H3G 1M8", "Description", "H",
                new LatLng(45.497711, -73.579035),
                new LatLng(45.497373, -73.578311),
                new LatLng(45.496829, -73.578850),
                new LatLng(45.497165, -73.579551));

        Building jm = new Building(mMap, "John Molson", "1450 Guy St, Montreal, Quebec H3H 0A1", "Description", "JM",
                new LatLng(45.495166, -73.579171),
                new LatLng(45.495222, -73.579113),
                new LatLng(45.495361, -73.579370),
                new LatLng(45.495531, -73.579196),
                new LatLng(45.495446, -73.578952),
                new LatLng(45.495188, -73.578517),
                new LatLng(45.495002, -73.578733),
                new LatLng(45.495032, -73.578789),
                new LatLng(45.495002, -73.578818),
                new LatLng(45.495166, -73.579171));

        Building gm = new Building(mMap, "GM", "1550 De Maisonneuve West, 1550 Boulevard de Maisonneuve O, Montreal, Quebec H3G 1N1", "Description", "GM",
                new LatLng(45.495780, -73.579145),
                new LatLng(45.496132, -73.578807),
                new LatLng(45.495947, -73.578429),
                new LatLng(45.495617, -73.578741),
                new LatLng(45.495780, -73.579087),
                new LatLng(45.495764, -73.579110),
                new LatLng(45.495780, -73.579145));

        Building ev = new Building(mMap, "EV", "1493-1515 Saint-Catherine St W, Montreal, Quebec H3G 2W1", "Description", "EV",
                new LatLng(45.495863, -73.578497),
                new LatLng(45.495440, -73.577609),
                new LatLng(45.495188, -73.577876),
                new LatLng(45.495247, -73.578019),
                new LatLng(45.495592, -73.578765),
                new LatLng(45.495863, -73.578497));

        Building lb = new Building(mMap, "Library Building", "Pavillion J.W. McConnell Bldg, 1400 Maisonneuve Blvd W, Montreal, Quebec H3G 1M8", "Description", "L",
                new LatLng(45.496729, -73.578579),
                new LatLng(45.497259, -73.578058),
                new LatLng(45.496893, -73.577294),
                new LatLng(45.496616, -73.577557),
                new LatLng(45.496637, -73.577602),
                new LatLng(45.496584, -73.577651),
                new LatLng(45.496496, -73.577466),
                new LatLng(45.496256, -73.577699),
                new LatLng(45.496668, -73.578567),
                new LatLng(45.496706, -73.578531),
                new LatLng(45.496729, -73.578579));

        Building td = new Building(mMap, "TD Building", "1410 Guy St, Montreal, Quebec H3H 2L7", "Description", "TD",
                new LatLng(45.495128, -73.578501),
                new LatLng(45.495189, -73.578428),
                new LatLng(45.495037, -73.578074),
                new LatLng(45.494944, -73.578176),
                new LatLng(45.495023, -73.578325),
                new LatLng(45.495043, -73.578301),
                new LatLng(45.495065, -73.578342),
                new LatLng(45.495048, -73.578365),
                new LatLng(45.495128, -73.578501));

        Building fg = new Building(mMap, "FG Building", "1616 Saint-Catherine St W, Montreal, Quebec H3H 1L7", "Description", "FG",
                new LatLng(45.494911, -73.577786),
                new LatLng(45.494655, -73.577222),
                new LatLng(45.494399, -73.577521),
                new LatLng(45.494453, -73.577620),
                new LatLng(45.493626, -73.578728),
                new LatLng(45.493823, -73.579067),
                new LatLng(45.494911, -73.577786));

        Building gn = new Building(mMap, "Grey Nun's Building", "1190 Guy St, Montreal, Quebec H3H 2L4", "Description", "GN",
                new LatLng(45.493974, -73.577561),
                new LatLng(45.494125, -73.577414),
                new LatLng(45.494095, -73.577349),
                new LatLng(45.494392, -73.577059),
                new LatLng(45.494020, -73.576281),
                new LatLng(45.494124, -73.576180),
                new LatLng(45.494034, -73.575996),
                new LatLng(45.493934, -73.576095),
                new LatLng(45.493713, -73.575641),
                new LatLng(45.493571, -73.575783),
                new LatLng(45.493792, -73.576244),
                new LatLng(45.493492, -73.576540),
                new LatLng(45.493470, -73.576499),
                new LatLng(45.493341, -73.576625),
                new LatLng(45.493364, -73.576674),
                new LatLng(45.493025, -73.577007),
                new LatLng(45.492733, -73.576396),
                new LatLng(45.492591, -73.576539),
                new LatLng(45.492741, -73.576861),
                new LatLng(45.492670, -73.576938),
                new LatLng(45.492711, -73.577033),
                new LatLng(45.492810, -73.576962),
                new LatLng(45.492896, -73.577142),
                new LatLng(45.492840, -73.577196),
                new LatLng(45.492927, -73.577379),
                new LatLng(45.492993, -73.577315),
                new LatLng(45.493088, -73.577510),
                new LatLng(45.493198, -73.577401),
                new LatLng(45.493108, -73.577212),
                new LatLng(45.493425, -73.576907),
                new LatLng(45.493530, -73.577129),
                new LatLng(45.493635, -73.577262),
                new LatLng(45.493747, -73.577157),
                new LatLng(45.493869, -73.577340));

        Building va = new Building(mMap, "VA Building", "1395 René-Lévesque Blvd W, Montreal, Quebec H3G 2M5", "Description", "VA",
                new LatLng(45.495672, -73.574309),
                new LatLng(45.496185, -73.573799),
                new LatLng(45.496070, -73.573563),
                new LatLng(45.495816, -73.573811),
                new LatLng(45.495667, -73.573506),
                new LatLng(45.495404, -73.573765),
                new LatLng(45.495672, -73.574309));

        return new Campus(
                new ArrayList<>(Arrays.asList(hall, jm, gm, ev, lb, td, fg, gn, va)),
                new LatLng(45.496680, -73.578761));

    }

    public Campus buildLayola() {
        Building ad = new Building(mMap, "Administration building", "Refectory, Montreal, QC H4B", "Description", "A",
                new LatLng(45.457912, -73.640122),
                new LatLng(45.457985, -73.640068),
                new LatLng(45.457962, -73.640005),
                new LatLng(45.458276, -73.639764),
                new LatLng(45.458298, -73.639821),
                new LatLng(45.458371, -73.639765),
                new LatLng(45.458258, -73.639469),
                new LatLng(45.458184, -73.639528),
                new LatLng(45.458203, -73.639617),
                new LatLng(45.458091, -73.639694),
                new LatLng(45.458063, -73.639634),
                new LatLng(45.458002, -73.639681),
                new LatLng(45.458023, -73.639748),
                new LatLng(45.457915, -73.639837),
                new LatLng(45.457875, -73.639771),
                new LatLng(45.457800, -73.639828),
                new LatLng(45.457912, -73.640122));

        Building cc = new Building(mMap, "Central Building", "7141 Rue Sherbrooke O, Montréal, QC H4B 2B5 Sherbrooke St W, Montreal, Quebec H4B 2B5", "Description", "C",
                new LatLng(45.458379, -73.640792),
                new LatLng(45.458524, -73.640678),
                new LatLng(45.458221, -73.639902),
                new LatLng(45.458082, -73.640014),
                new LatLng(45.458379, -73.640792));

        Building rf = new Building(mMap, "Jesuit hall and conference centre", "7141 Sherbrooke St W, Montreal, Quebec H4B 1R6", "Description", "JH",
                new LatLng(45.458508, -73.641373),
                new LatLng(45.458805, -73.641159),
                new LatLng(45.458683, -73.640806),
                new LatLng(45.458587, -73.640881),
                new LatLng(45.458540, -73.640756),
                new LatLng(45.458415, -73.640854),
                new LatLng(45.458474, -73.641007),
                new LatLng(45.458383, -73.641079),
                new LatLng(45.458429, -73.641199),
                new LatLng(45.458510, -73.641137),
                new LatLng(45.458547, -73.641231),
                new LatLng(45.458527, -73.641249),
                new LatLng(45.458538, -73.641278),
                new LatLng(45.458486, -73.641319),
                new LatLng(45.458508, -73.641373));

        Building py = new Building(mMap, "Psychology Building", "Refectory, Montreal, Quebec H4B 2Z3", "Description", "P",
                new LatLng(45.458853, -73.640830),
                new LatLng(45.459181, -73.640575),
                new LatLng(45.459199, -73.640624),
                new LatLng(45.459282, -73.640560),
                new LatLng(45.459119, -73.640136),
                new LatLng(45.458725, -73.640456),
                new LatLng(45.458819, -73.640696),
                new LatLng(45.458801, -73.640714),
                new LatLng(45.458853, -73.640830));

        return new Campus(
                new ArrayList<>(Arrays.asList(ad, cc, rf, py)),
                new LatLng(45.458239, -73.640462));
    }
}
