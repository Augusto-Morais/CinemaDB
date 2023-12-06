package DB.TablesSetUp;

import Data.Studio;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class StudioSetup {

    private static final String sqlFilesDir = System.getProperty("user.dir") + "\\src\\main\\SQLFiles\\Studio\\";
    public static ArrayList<Studio> scrapeStudios(){

        ArrayList<Studio> studios = new ArrayList<>();
        ArrayList<String> majorStudiosLinks = new ArrayList<>();
        ArrayList<String> miniMajorStudiosLinks = new ArrayList<>();

        try {
            Document studiosListDoc = Jsoup.connect("https://en.wikipedia.org/wiki/Major_film_studios")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                            "OPR/102.0.0.0")
                    .header("Accept-Language","*")


                    .get();

//            MAJOR STUDIOS
            Elements majorStudiosRows = studiosListDoc.select("table[class='wikitable sortable']").get(0).select("tbody tr");
            majorStudiosRows.remove(0);


            for (Element row:
                    majorStudiosRows) {

//                STUDIO LINK
                int numStudios = row.select("td").get(1).select("a").size();


                for (int i = 0; i < numStudios; i++) {
                    String studioLink = row.select("td").get(1).select("a").get(i)
                            .attr("href");
                    majorStudiosLinks.add("https://en.wikipedia.org" + studioLink);
//                    System.out.println(studioLink);
                }

            }

//            MINI MAJOR STUDIOS
            Elements miniMajorStudiosRows = studiosListDoc.select("table[class='wikitable sortable']").get(1).select("tbody tr");
            miniMajorStudiosRows.remove(0);
//            System.out.println(miniMajorStudiosRows);


            for (Element row:
                 miniMajorStudiosRows) {

//                STUDIO LINK
//                System.out.println(row.select("td").get(1).select("a") + "\t" + row.select("td").get(1).hasAttr("href"));
                Elements linkSet =  !row.select("td").get(1).select("a").isEmpty() ?
                        row.select("td").get(1).select("a") :
                        row.select("td").get(0).select("a");
//                System.out.println(linkSet + "\t" + linkSet.size());

                for (Element element : linkSet) {
                    if (!element.attr("href").contains("#cite_note")) {
                        miniMajorStudiosLinks.add("https://en.wikipedia.org" + element.attr("href"));
                    }
                }
             }

//            System.out.println(miniMajorStudiosLinks);
//            System.out.println(studiosLinks);

            ArrayList<String> studioLinks = new ArrayList<>();
            studioLinks.addAll(miniMajorStudiosLinks);
            studioLinks.addAll(majorStudiosLinks);
//            System.out.println(studioLinks);
//            System.out.println(studioLinks.size());



            for (String studioLink:
                    studioLinks) {

                Studio studio = new Studio();

                Document studioDoc = Jsoup.connect(studioLink)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                                "OPR/102.0.0.0")
                        .header("Accept-Language","*")
                        .get();


                Elements infobox = studioDoc.select("table[class='infobox vcard']");

//                STUDIO LOGO
                String logoLink = infobox.select("tbody tr td[class='infobox-image logo'] img").attr("src");
                studio.setLogoLink(logoLink);

//                TRADE NAME
                String tradeName = infobox.select("caption").text();
                studio.setTradeName(tradeName);

//                DATE FOUNDED
                String dateFounded = infobox.select("tbody tr th:contains(Founded) + td").text();
                studio.setDateFounded(dateFounded);
//                System.out.println(tradeName);

//                FOUNDERS
                String founders = ""; 
                
                if(!infobox.select("tbody tr th:contains(Founders) + td").isEmpty())
                {
                    founders = infobox.select("tbody tr th:contains(Founders) + td").text();
                } else if (!infobox.select("tbody tr th:contains(Founder) + td").isEmpty()) {
                    founders = infobox.select("tbody tr th:contains(Founder) + td").text();
                }
                studio.setFounders(founders);

//                HEADQUARTERS
                String headquarters = infobox.select("tbody tr th:contains(Headquarters) + td").text();
                studio.setHeadquarters(headquarters);

//                GENERAL INFO
                ArrayList<String> generalInfo = new ArrayList<>();

                Element endOfGeneralInfo = !studioDoc.select("table[class='infobox biography vcard'] ~ h2 span:first-child").isEmpty()?
                        studioDoc.select("table[class='infobox biography vcard'] ~ h2 span:first-child").get(0):
                        new Element("undefined");
                String endOfGeneralInfoId = endOfGeneralInfo.attr("id");

//                Elements generalInfoParagraphs = studioDoc.select("table[class='infobox vcard'] ~ :not(h2:has(span[id='" + endOfGeneralInfoId +  "']), h2:has(span[id='" + endOfGeneralInfoId +  "']) ~ *)");
                Elements generalInfoParagraphs = studioDoc.select("table[class='infobox vcard'] ~ p:not(h2,h2 ~ *)");
//                generalInfoParagraphs.remove(generalInfoParagraphs.size() - 1);

//                System.out.println(generalInfoParagraphs);
//                System.out.println("====================================\t" + endOfGeneralInfoId);
//                System.out.println();

                for (Element generalInfoParagraph : generalInfoParagraphs) {
                    generalInfo.add(generalInfoParagraph.text().replace('\'','"'));
                }
                studio.setGeneralInfo(generalInfo);

//                table[class='infobox biography vcard']  ~ :not(h2:has(span[id='" + endOfGeneralInfoId +  "']),h2:has(span[id='" + endOfGeneralInfoId +  "']) ~ *)


//                System.out.println(studio);
                studios.add(studio);
            }

//            System.out.println(studios);



        } catch (IOException e) {
            e.printStackTrace();
        }


        return studios;
    }

    public static void fillStudioTable(ArrayList<Studio> studios){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sqlFilesDir + "fillStudioTable.sql",true))) {

            for (Studio studio:
                 studios) {
                bufferedWriter.write(String.format(
                        "('%s', '%s', '%s', '%s', '%s', ARRAY ["
                , studio.getTradeName(), studio.getLogoLink(), studio.getDateFounded(),
                        studio.getFounders(),studio.getHeadquarters()));

                for (String generalInfoParagraph:
                        studio.getGeneralInfo()) {
                    if(studio.getGeneralInfo().indexOf(generalInfoParagraph) == studio.getGeneralInfo().size() -1)
                    {
                        bufferedWriter.write(String.format(
                                "'%s']),\n", generalInfoParagraph.replaceAll("\\[\\d]","")
                                )
                        );
                    }
                    else{
                        bufferedWriter.write(String.format(
                                        "'%s', ", generalInfoParagraph.replaceAll("\\[\\d]","")
                                )
                        );
                    }
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArrayList<Studio> studios = scrapeStudios();
        fillStudioTable(studios);
    }
}
