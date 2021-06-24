package oneForm;

import java.util.HashMap;
import java.util.Map;

/*********************************************

This is a mapping for the tags in each department compared to the
 tags of the one form.

 Each tag in the one form should always match up with a tag in a
 department.

 Each entry of the designated map contains the first value which is
 the ID of the one form tag. (The key) The second value is the ID of
 the corresponding tag in the department (The value)

 * @author Robby Breidenbaugh
 * @since 12/21/20
*********************************************/

public class tagMaps {
    Map<String, String> academicMap = new HashMap<String, String>() {{
        put("31788", "6199");
        put("31789", "6200");
        put("31797", "17653");
        put("31790", "13951");
        put("31791", "13952");
        put("31792", "6214");
        put("31793", "6230");
        put("31794", "6240");
        put("31795", "6243");
        put("31808", "6210");
        put("31800", "6235");
        put("31801", "20104");
        put("31802", "20105");
        put("31803", "13953");
        put("31804", "19923");
        put("31805", "6244");
        put("31806", "6248");
        put("31796", "33993");
        put("31826", "6238");

    }};
    Map<String, String> accountingMap = new HashMap<String, String>() {{
        put("31999", "22985");
        put("32000", "29104");
        put("31997", "29350");
        put("32002", "22999");
        put("32003", "22996");
        put("32004", "22997");
        put("32005", "22998");
        put("31994", "29348");
        put("32006", "22993");
        put("32007", "22988");
        put("32008", "22987");
        put("32009", "29372");
        put("32010", "22992");
        put("32011", "22989");
        put("32001", "22986");
        put("32012", "22990");
        put("32013", "29058");
        put("32014", "22994");
        put("31995", "29349");
        put("31996", "29351");
        put("32015", "23000");
        put("31998", "29347");
        put("32017", "22991");
        put("31826", "23001");

    }};
    Map<String, String> admissionsMap = new HashMap<String, String>() {{
        put("31966", "15629");
        put("31967", "15630");
        put("31980", "15642");
        put("31968", "15631");
        put("31969", "15632");
        put("31970", "17015");
        put("31971", "15633");
        put("31972", "15634");
        put("31973", "15636");
        put("31974", "15637");
        put("31975", "24502");
        put("31976", "15638");
        put("31977", "15639");
        put("31978", "15640");
        put("31979", "15641");
        put("31981", "15643");
        put("31982", "15644");
        put("31983", "15645");
        put("31984", "29562");
        put("31985", "15646");
        put("31986", "15647");
        put("31987", "15648");
        put("31988", "15649");
        put("31989", "15635");
        put("31990", "15650");
        put("31991", "15651");
        put("31992", "15652");
        put("31993", "16371");
        put("31826", "15653");

    }};
    Map<String, String> advisingCampusMap = new HashMap<String, String>() {{
        put("31953", "19630");
        put("31954", "11291");
        put("31955", "8847");
        put("31965", "6048");
        put("31956", "33994");
        put("31957", "6051");
        put("31958", "6054");
        put("31959", "14205");
        put("31961", "6057");
        put("31962", "22629");
        put("31963", "19416");
        put("31964", "17073");
        put("31826", "6062");
        put("31960", "6056");

    }};
    Map<String, String> advisingOnlineMap = new HashMap<String, String>() {{
        put("31953", "19630");
        put("31954", "11291");
        put("31955", "8847");
        put("31965", "6048");
        put("31956", "33994");
        put("31957", "6051");
        put("31958", "6054");
        put("31959", "14205");
        put("31961", "6057");
        put("31962", "22629");
        put("31963", "19416");
        put("31964", "17073");
        put("31826", "6062");

    }};
    Map<String, String> facilitiesMap = new HashMap<String, String>() {{
        put("31952", "19535");
        put("31945", "6209");
        put("31946", "6217");
        put("31947", "6219");
        put("33595", "33988");
        put("31948", "14672");
        put("31899", "6231");
        put("33598", "33989");
        put("31949", "6380");
        put("31950", "33990");
        put("33596", "33991");
        put("31951", "6239");
        put("33597", "33992");
        put("31826", "6238");

    }};
    Map<String, String> financialAidMap = new HashMap<String, String>() {{
        put("31921", "30804");
        put("31922", "21277");
        put("31924", "20804");
        put("31944", "33995");
        put("31936", "23169");
        put("31937", "19984");
        put("31925", "20803");
        put("31938", "19985");
        put("31926", "31300");
        put("31927", "19976");
        put("31928", "31301");
        put("31929", "19977");
        put("31930", "19978");
        put("31931", "31314");
        put("31923", "19975");
        put("31933", "19981");
        put("31932", "19980");
        put("31934", "19982");
        put("31935", "19983");
        put("31939", "19986");
        put("31941", "19991");
        put("31942", "20697");
        put("31943", "19992");
        put("31940", "19987");
        put("31826", "19989");

    }};
    Map<String, String> generalMap = new HashMap<String, String>() {{
        put("31877", "13950");
        put("31878", "14671");
        put("31879", "16170");
        put("31881", "6204");
        put("31882", "6205");
        put("31880", "33996");
        put("31883", "24244");
        put("31884", "6211");
        put("31885", "6212");
        put("31886", "6213");
        put("31887", "6241");
        put("31888", "6215");
        put("31889", "6216");
        put("31890", "20037");
        put("31891", "6220");
        put("31892", "6221");
        put("31893", "6224");
        put("31895", "21005");
        put("31894", "6225");
        put("33587", "33997");
        put("31896", "16669");
        put("31902", "17613");
        put("33585", "6226");
        put("33588", "33998");
        put("33586", "6227");
        put("31897", "6229");
        put("31898", "19962");
        put("31901", "29103");
        put("31903", "14673");
        put("31904", "14232");
        put("31905", "6246");
        put("31906", "6249");
        put("31907", "6250");
        put("31908", "6251");
        put("33593", "6259");
        put("31909", "22804");
        put("31910", "6254");
        put("31911", "13954");
        put("31912", "13955");
        put("31913", "6256");
        put("31914", "13956");
        put("31915", "13957");
        put("31916", "6261");
        put("31917", "6263");
        put("31918", "21229");
        put("31919", "16829");
        put("31920", "22949");
        put("31826", "6238");
        put("34472", "34473");
        put("34471", "34474");

    }};

    //THIS NEEDS SOME EXTRA WORK BECAUSE OF THE COuNSELING
    Map<String, String> healthCenterMap = new HashMap<String, String>() {{
        put("31864", "6131");
        put("31876", "33999");
        put("31875", "31543");
        put("31865", "6116");
        put("31866", "6117");
        put("31867", "6125");
        put("31873", "6128");
        put("31869", "6122");
        put("31872", "6127");
        put("31868", "6121");
        put("31870", "6124");
        put("31871", "6126");
        put("31874", "6130");
        put("31826", "6132");

    }};
    Map<String, String> itMap = new HashMap<String, String>() {{
        put("31862", "18784");
        put("31863", "31613");
        put("31856", "20799");
        put("31857", "18521");
        put("31858", "19296");
        put("31859", "18522");
        put("31860", "18519");
        put("31861", "29106");
        put("31826", "18520");

    }};
    Map<String, String> srrMap = new HashMap<String, String>() {{
        put("31827", "15898");
        put("31847", "15907");
        put("31828", "15899");
        put("31829", "31655");
        put("31830", "31657");
        put("31831", "31656");
        put("31832", "15900");
        put("31833", "17023");
        put("31834", "20466");
        put("31835", "15901");
        put("31855", "24097");
        put("31836", "15902");
        put("31837", "15903");
        put("31840", "20462");
        put("31841", "20461");
        put("31839", "15904");
        put("31844", "31653");
        put("31842", "20464");
        put("31843", "15905");
        put("31845", "20465");
        put("31846", "15906");
        put("31838", "31654");
        put("31848", "15909");
        put("31849", "15910");
        put("31850", "15911");
        put("31851", "15912");
        put("31852", "15913");
        put("31853", "15914");
        put("31854", "15915");
        put("31826", "15917");

    }};
    Map<String, String> universityStoreMap = new HashMap<String, String>() {{
        put("31809", "16193");
        put("31810", "6177");
        put("31811", "6178");
        put("31812", "6179");
        put("31813", "6180");
        put("31814", "6189");
        put("31815", "6182");
        put("31816", "13993");
        put("32065", "6184");
        put("31817", "15598");
        put("31818", "6186");
        put("31819", "16240");
        put("31820", "15706");
        put("31821", "6190");
        put("31822", "6181");
        put("31823", "6191");
        put("31824", "6193");
        put("31825", "6194");
        put("31826", "6195");

    }};

}