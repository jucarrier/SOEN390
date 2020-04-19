package Helpers;

import com.example.concordiaguide.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import Models.Building;
import Models.Campus;
import Models.Floor;
import Models.GatewayNodes;

public class CampusBuilder {
    GoogleMap mMap;
    GatewayNodes basicGatewayNode;
    private String stairNode = "Stairs";
    private String elevatorOneNode = "Elevator 1";
    private final String defaultDescription = "Description";
    private final String deafultLayolaAddress = "7141 Sherbrooke St W, Montreal, Quebec H4B 1R6";

    public CampusBuilder(GoogleMap mMap) {
        basicGatewayNode = new GatewayNodes(elevatorOneNode, elevatorOneNode, stairNode, stairNode, null);
        this.mMap = mMap;
    }

    public Campus buildSGW() {

        Building h = new Building(mMap, "Hall", "1455 Boulevard de Maisonneuve O, Montréal, QC H3G 1M8", defaultDescription, "H",
                new Floor[]{
                        new Floor("8th", 8, R.drawable.ic_hall_8, basicGatewayNode,
                                new String[]{"H867", "H801", "H803", "H805.03", "H805.02", "H805.01",
                                        "H807", "H811", "H813", "H815", "H817", "H819", "H821", "H823",
                                        "H825", "H827", "H829", "H831", "H833", "H835", "H837", "H841",
                                        "H843", "H845", "H847", "H849", "H851.03", "H851.02", "H851.01",
                                        "H853", "H855", "H857", "H859", "H861", "H865", "H863", "H899.51",
                                        stairNode, "Washroom - Women", elevatorOneNode, "Elevator 2", "H806.03",
                                        "H806.02", "H806.01", "Washroom - Staff", "H840", "H838", "H862",
                                        "H860.04", "H860.06", "H860.03", "H860.01", "H860.05", "H854",
                                        "H842", "H852", "H881", "H838", "Washroom - Men", "H886", "H854",
                                        "H820", "H822", "H832.02", "H832.05", "H832.03", "H832.01",
                                        "H832.06", "H802.01", "H898", "838.01"}),
                        new Floor("9th", 9, R.drawable.ic_hall_9, basicGatewayNode,
                                new String[]{"H927-1", "H927-2", "H925", "H925-1", "H925-3", "H923",
                                        "H921", "H919", "H917", "H913", "H911", "H909", "H907", "H903",
                                        "H967", "H965", "H961-1", "H961-3", "H961-7", "H961-9",
                                        "H961-11", "H961-13", "H961-15", "H961-17", "H961-21",
                                        "H961-23", "H961-25", "H961-27", "H961-29", "H961-31",
                                        "H961-26", "H961-14", "H961-12", "H961-10", "H945", "H975",
                                        "H981", "H977", "H961-28", "H961-30", "H943", "H961-33",
                                        "H929", "H931", "H933-1", "H933-11", "H933-2", "H929-25",
                                        "H941", "H933", "H937", "H937-1", "H937-3", "H983", "H928",
                                        "H932", "H985", "H932-1", "H927-4", "H927", "H915", "H920",
                                        "H920-1", "H980", "Washroom - Men", "H990", "H986",
                                        "H961-97", "H961-6", "H961-4", "H961-2", "H963", "H961-8",
                                        "H968", "H964-3", "H963-95", "H964-2", "H964-1", "H966",
                                        "H966-1", "H966-2", "H962-1", "H960", "H962", "H964", "H998",
                                        elevatorOneNode, "Elevator 2", "Washroom - Staff", "H908",
                                        "Washroom - Women", "H902-1", "H906", "H961-19"})},
                new LatLng(45.497711, -73.579035),
                new LatLng(45.497373, -73.578311),
                new LatLng(45.496829, -73.578850),
                new LatLng(45.497165, -73.579551));

        Building mb = new Building(mMap, "John Molson", "1450 Guy St, Montreal, Quebec H3H 0A1", defaultDescription, "JM",
                new Floor[]{
                        new Floor("1st", 1, R.drawable.ic_mb_1, basicGatewayNode,
                                new String[]{"MB1.294", "MB1.210", "MB1.338", "MB1.310", "MB1.437", "MB1.301", "MB1.309", "MB1.394", "MB1.335",
                                        "MB1.315", "MB1.424", "MB1.410", "MB1.426", "MB1.430", "MB1.436", "MB1.435", "MB1.494", "MB1.115",
                                        "MB1.434", "MB1.132", "MB1.130", "MB1.134", "MB1.299"}),
                        new Floor("S2", -2, R.drawable.ic_mb_s2, basicGatewayNode,
                                new String[]{"MBS2.245", "MBS2.294", "MBS2.273", "MBS2.275", "MBS2.279", "MBS2.285", "MBS2.330", "MBS2.210", "MBS2.345",
                                        "MBS2.230", "MBS2.310", "MBS2.394", "MBS2.438", "MBS2.440", "MBS2.418", "MBS2.420", "MBS2.410", "MBS2.437", "MBS2.435",
                                        "MBS2.428", "MBS2.445", "MBS2.455", "MBS2.465", "MBS2.470", "MBS2.401", "MBS2.105", "MBS2.115", "MBS2.135", "MBS2.145"})
                },
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

        Building gm = new Building(mMap, "GM", "1550 De Maisonneuve West, 1550 Boulevard de Maisonneuve O, Montreal, Quebec H3G 1N1", defaultDescription, "GM",
                new Floor[]{},
                new LatLng(45.495780, -73.579145),
                new LatLng(45.496132, -73.578807),
                new LatLng(45.495947, -73.578429),
                new LatLng(45.495617, -73.578741),
                new LatLng(45.495780, -73.579087),
                new LatLng(45.495764, -73.579110),
                new LatLng(45.495780, -73.579145));

        Building ev = new Building(mMap, "EV", "1493-1515 Saint-Catherine St W, Montreal, Quebec H3G 2W1", defaultDescription, "EV",
                new Floor[]{},
                new LatLng(45.495863, -73.578497),
                new LatLng(45.495440, -73.577609),
                new LatLng(45.495188, -73.577876),
                new LatLng(45.495247, -73.578019),
                new LatLng(45.495592, -73.578765),
                new LatLng(45.495863, -73.578497));

        Building lb = new Building(mMap, "Library Building", "Pavillion J.W. McConnell Bldg, 1400 Maisonneuve Blvd W, Montreal, Quebec H3G 1M8", defaultDescription, "LB",
                new Floor[]{},
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

        Building td = new Building(mMap, "TD Building", "1410 Guy St, Montreal, Quebec H3H 2L7", defaultDescription, "TD",
                new Floor[]{},
                new LatLng(45.495128, -73.578501),
                new LatLng(45.495189, -73.578428),
                new LatLng(45.495037, -73.578074),
                new LatLng(45.494944, -73.578176),
                new LatLng(45.495023, -73.578325),
                new LatLng(45.495043, -73.578301),
                new LatLng(45.495065, -73.578342),
                new LatLng(45.495048, -73.578365),
                new LatLng(45.495128, -73.578501));

        Building fg = new Building(mMap, "FG Building", "1616 Saint-Catherine St W, Montreal, Quebec H3H 1L7", defaultDescription, "FG",
                new Floor[]{},
                new LatLng(45.494911, -73.577786),
                new LatLng(45.494655, -73.577222),
                new LatLng(45.494399, -73.577521),
                new LatLng(45.494453, -73.577620),
                new LatLng(45.493626, -73.578728),
                new LatLng(45.493823, -73.579067),
                new LatLng(45.494911, -73.577786));

        Building gn = new Building(mMap, "Grey Nun's Building", "1190 Guy St, Montreal, Quebec H3H 2L4", defaultDescription, "GN",
                new Floor[]{},
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

        Building va = new Building(mMap, "VA Building", "1395 René-Lévesque Blvd W, Montreal, Quebec H3G 2M5", defaultDescription, "VA",
                new Floor[]{},
                new LatLng(45.495672, -73.574309),
                new LatLng(45.496185, -73.573799),
                new LatLng(45.496070, -73.573563),
                new LatLng(45.495816, -73.573811),
                new LatLng(45.495667, -73.573506),
                new LatLng(45.495404, -73.573765),
                new LatLng(45.495672, -73.574309));

        return new Campus(
                new ArrayList<>(Arrays.asList(h, mb, gm, ev, lb, td, fg, gn, va)),
                new LatLng(45.496680, -73.578761));

    }

    public Campus buildLoyola() {

        Building ad = new Building(mMap, "Administration building", "Refectory, Montreal, QC H4B", defaultDescription, "AD",
                new Floor[]{},
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

        Building cc = new Building(mMap, "Central Building", deafultLayolaAddress, defaultDescription, "CC",
                new Floor[]{},
                new LatLng(45.458379, -73.640792),
                new LatLng(45.458524, -73.640678),
                new LatLng(45.458221, -73.639902),
                new LatLng(45.458082, -73.640014),
                new LatLng(45.458379, -73.640792));

        Building rf = new Building(mMap, "Jesuit hall and conference centre", deafultLayolaAddress, defaultDescription, "RF",
                new Floor[]{},
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

        Building py = new Building(mMap, "Psychology Building", "Refectory, Montreal, Quebec H4B 2Z3", defaultDescription, "PY",
                new Floor[]{},
                new LatLng(45.458853, -73.640830),
                new LatLng(45.459181, -73.640575),
                new LatLng(45.459199, -73.640624),
                new LatLng(45.459282, -73.640560),
                new LatLng(45.459119, -73.640136),
                new LatLng(45.458725, -73.640456),
                new LatLng(45.458819, -73.640696),
                new LatLng(45.458801, -73.640714),
                new LatLng(45.458853, -73.640830));

        Building fc = new Building(mMap, "F.C. Smith building", deafultLayolaAddress, defaultDescription, "FC",
                new Floor[]{},
                new LatLng(45.458737, -73.639486),
                new LatLng(45.458569, -73.639050),
                new LatLng(45.458426, -73.639159),
                new LatLng(45.458591, -73.639595),
                new LatLng(45.458737, -73.639486));

        Building sp = new Building(mMap, "Richard J. Renaud Science Complex", "3475 Rue West Broadway Montreal, QC H4B 2A7", defaultDescription, "SP",
                new Floor[]{},
                new LatLng(45.456985, -73.640827),
                new LatLng(45.457439, -73.642003),
                new LatLng(45.457641, -73.641846),
                new LatLng(45.457672, -73.641925),
                new LatLng(45.458326, -73.641413),
                new LatLng(45.458276, -73.641284),
                new LatLng(45.458209, -73.641337),
                new LatLng(45.458179, -73.641262),
                new LatLng(45.458256, -73.641203),
                new LatLng(45.458194, -73.641040),
                new LatLng(45.458338, -73.640923),
                new LatLng(45.458315, -73.640864),
                new LatLng(45.457997, -73.641113),
                new LatLng(45.457976, -73.641065),
                new LatLng(45.457891, -73.641133),
                new LatLng(45.457908, -73.641170),
                new LatLng(45.457524, -73.641472),
                new LatLng(45.457200, -73.640656),
                new LatLng(45.456985, -73.640827));

        Building cj = new Building(mMap, "Communication studies and Journalism building", deafultLayolaAddress, defaultDescription, "CJ",
                new Floor[]{},
                new LatLng(45.457334, -73.640716),
                new LatLng(45.457597, -73.640502),
                new LatLng(45.457650, -73.640630),
                new LatLng(45.457829, -73.640486),
                new LatLng(45.457754, -73.640291),
                new LatLng(45.457726, -73.640316),
                new LatLng(45.457622, -73.640046),
                new LatLng(45.457485, -73.640151),
                new LatLng(45.457435, -73.640029),
                new LatLng(45.457480, -73.639820),
                new LatLng(45.457428, -73.639771),
                new LatLng(45.457330, -73.639770),
                new LatLng(45.457279, -73.639804),
                new LatLng(45.457230, -73.639883),
                new LatLng(45.457216, -73.640019),
                new LatLng(45.457360, -73.640076),
                new LatLng(45.457410, -73.640206),
                new LatLng(45.457177, -73.640395),
                new LatLng(45.457279, -73.640659),
                new LatLng(45.457303, -73.640638),
                new LatLng(45.457334, -73.640716));

        Building vl = new Building(mMap, "Vanier Library", deafultLayolaAddress, defaultDescription, "VL",
                new Floor[]{},
                new LatLng(45.459107, -73.639410),
                new LatLng(45.459216, -73.639334),
                new LatLng(45.459234, -73.639380),
                new LatLng(45.459304, -73.639323),
                new LatLng(45.459264, -73.639221),
                new LatLng(45.459316, -73.639180),
                new LatLng(45.459340, -73.639241),
                new LatLng(45.459483, -73.639135),
                new LatLng(45.459300, -73.638673),
                new LatLng(45.459311, -73.638664),
                new LatLng(45.459132, -73.638197),
                new LatLng(45.459212, -73.638133),
                new LatLng(45.459103, -73.637848),
                new LatLng(45.458903, -73.638002),
                new LatLng(45.458845, -73.638255),
                new LatLng(45.458705, -73.638364),
                new LatLng(45.458717, -73.638402),
                new LatLng(45.458631, -73.638468),
                new LatLng(45.458845, -73.639024),
                new LatLng(45.459049, -73.638867),
                new LatLng(45.459091, -73.638969),
                new LatLng(45.459079, -73.638998),
                new LatLng(45.459095, -73.639042),
                new LatLng(45.458992, -73.639126),
                new LatLng(45.458992, -73.639126),
                new LatLng(45.459107, -73.639410));

        Building ra = new Building(mMap, "Sports Complex", "7200 Sherbrooke St W, Montreal, Quebec H4B 1R2", defaultDescription, "RA",
                new Floor[]{},
                new LatLng(45.456889, -73.638556),
                new LatLng(45.457157, -73.638339),
                new LatLng(45.457038, -73.638036),
                new LatLng(45.457011, -73.638055),
                new LatLng(45.456958, -73.637928),
                new LatLng(45.457026, -73.637873),
                new LatLng(45.457015, -73.637843),
                new LatLng(45.457283, -73.637629),
                new LatLng(45.456949, -73.636776),
                new LatLng(45.456683, -73.636993),
                new LatLng(45.456727, -73.637102),
                new LatLng(45.456392, -73.637373),
                new LatLng(45.456694, -73.638141),
                new LatLng(45.456794, -73.638060),
                new LatLng(45.456844, -73.638188),
                new LatLng(45.456775, -73.638246),
                new LatLng(45.456802, -73.638317),
                new LatLng(45.456768, -73.638345),
                new LatLng(45.456805, -73.638444),
                new LatLng(45.456833, -73.638416),
                new LatLng(45.456889, -73.638556));

        Building ge = new Building(mMap, "Centre for structural and functional Genomics", "7079 Rue de Terrebonne, Montréal, QC H4B 2B4 Rue de Terrebonne, Montréal, QC H4B 2B4, Canada", defaultDescription, "GE",
                new Floor[]{},
                new LatLng(45.456946, -73.640740),
                new LatLng(45.457174, -73.640571),
                new LatLng(45.457041, -73.640166),
                new LatLng(45.456800, -73.640349),
                new LatLng(45.456895, -73.640611),
                new LatLng(45.456873, -73.640630),
                new LatLng(45.456894, -73.640689),
                new LatLng(45.456919, -73.640670),
                new LatLng(45.456946, -73.640740));

        Building cs = new Building(mMap, "Concordia Stadium", deafultLayolaAddress, defaultDescription, "CS",
                new Floor[]{},
                new LatLng(45.457832, -73.638341),
                new LatLng(45.458802, -73.637197),
                new LatLng(45.458379, -73.636428),
                new LatLng(45.457393, -73.637585),
                new LatLng(45.457832, -73.638341));

        Building sd = new Building(mMap, "Stinger Dome", "7200 Sherbrooke St W, Montreal, Quebec H4B 1R2", defaultDescription, "DO",
                new Floor[]{},
                new LatLng(45.457372, -73.637088),
                new LatLng(45.458335, -73.635960),
                new LatLng(45.457930, -73.635241),
                new LatLng(45.456961, -73.636358),
                new LatLng(45.457372, -73.637088));

        return new Campus(
                new ArrayList<>(Arrays.asList(ad, cc, rf, py, fc, sp, cj, vl, ra, ge, cs, sd)),
                new LatLng(45.458239, -73.640462));
    }
}
