package utilities.reports;


import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class GenerateSummary {

    @SneakyThrows
    public static org.jsoup.nodes.Document parseSummaryReport(String strInputHTML) {
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse(new File(strInputHTML), "UTF-8");
        jsoupDocument.outputSettings().prettyPrint(false).indentAmount(0);

        jsoupDocument.head().remove();

        jsoupDocument.body().select("nav#navigation").remove();
        jsoupDocument.body().select("div#charts").remove();
        jsoupDocument.body().select("nav#footer").remove();

        jsoupDocument.body().select("div.container-fluid").attr("style","width: fit-content;");
        jsoupDocument.body().select("div.container-fluid:not([id])>div").attr("class","col-md-5 col-md-offset-0");
        jsoupDocument.body().select("table#build-info").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;border-collapse: collapse!important;border-spacing: 0;background-color: transparent;width: 100%;max-width: 100%;margin-bottom: 20px;border: 1px solid #ddd;");
        jsoupDocument.body().select("table>thead").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;display: table-header-group;");
        jsoupDocument.body().select("table#build-info tr").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;page-break-inside: avoid;");
        jsoupDocument.body().select("table#build-info>thead>tr>th").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 8px;text-align: left;line-height: 1.42857143;vertical-align: bottom;border-top: 1px solid #ddd;border-bottom: 2px solid #ddd;border: 1px solid #ddd!important;border-bottom-width: 2px;background-color: #fff!important;");
        jsoupDocument.body().select("table#build-info td").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 8px;line-height: 1.42857143;vertical-align: top;border-top: 1px solid #ddd;border: 1px solid #ddd!important;background-color: #fff!important;");
        jsoupDocument.body().select("table >tbody").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;");

        jsoupDocument.body().select("div#report-lead>div").attr("class","col-md-10 col-md-offset-0");

        Element tableClassification = jsoupDocument.select("table#classifications").first();
        if (tableClassification != null) {
            jsoupDocument.body().select("div.container-fluid:not([id])>div>table#build-info").after(String.valueOf(tableClassification));
        }
        jsoupDocument.body().select("div.container-fluid:not([id])>div:nth-child(2)").remove();
        jsoupDocument.body().select("br").remove();

        jsoupDocument.body().select("div#report>div>div").attr("class","col-md-10 col-md-offset-0");

        jsoupDocument.body().select("table#tablesorter").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;border-collapse: collapse;border-spacing: 0;background-color: white;color: black;margin-bottom: 20px;width: 100%;");
        jsoupDocument.body().select("table#tablesorter>thead>tr:first-child").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;page-break-inside: avoid;background-color: #66CCEE;");
        jsoupDocument.body().select("table#tablesorter>thead tr:first-child>th").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;text-align: center;border: 1px solid gray;");
        jsoupDocument.body().select("table#tablesorter>thead>tr:last-child").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;page-break-inside: avoid;");
        jsoupDocument.body().select("table#tablesorter>thead tr:last-child>th:not([class])").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;text-align: center;border: 1px solid gray;cursor: pointer;");

        jsoupDocument.body().select("table#tablesorter>thead tr:last-child>th.passed").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;text-align: center;background-color: #92DD96;border: 1px solid gray;cursor: pointer;");
        jsoupDocument.body().select("table#tablesorter>thead tr:last-child>th.failed").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;text-align: center;background-color: #F2928C;border: 1px solid gray;cursor: pointer;");
        jsoupDocument.body().select("table#tablesorter>thead tr:last-child>th.skipped").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;text-align: center;background-color: #8AF;border: 1px solid gray;cursor: pointer;");
        jsoupDocument.body().select("table#tablesorter>thead tr:last-child>th.pending").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;text-align: center;background-color: #F5F28F;border: 1px solid gray;cursor: pointer;");
        jsoupDocument.body().select("table#tablesorter>thead tr:last-child>th.undefined").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;text-align: center;background-color: #F5B975;border: 1px solid gray;cursor: pointer;");
        jsoupDocument.body().select("table#tablesorter>thead tr:last-child>th.total").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;text-align: center;background-color: lightgray;border: 1px solid gray;cursor: pointer;");

        jsoupDocument.body().select("table#tablesorter>tbody>tr").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;page-break-inside: avoid;");
        jsoupDocument.body().select("table#tablesorter>tbody>tr>td:not([class])").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;border: 1px solid gray;text-align: center;");
        jsoupDocument.body().select("table#tablesorter>tbody>tr>td.tagname").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;border: 1px solid gray;text-align: left;");
        jsoupDocument.body().select("table#tablesorter>tbody>tr>td.passed").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;background-color: #92DD96;border: 1px solid gray;text-align: center;");
        jsoupDocument.body().select("table#tablesorter>tbody>tr>td.failed").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;background-color: #F2928C;border: 1px solid gray;text-align: center;");

        jsoupDocument.body().select("table#tablesorter>tbody>tr>td.total").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;background-color: lightgray;border: 1px solid gray;text-align: center;");
        jsoupDocument.body().select("table#tablesorter>tbody>tr>td.duration").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;border: 1px solid gray;text-align: right;white-space: nowrap;");

        jsoupDocument.body().select("tfoot.total").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;background-color: lightgray;font-weight: bold;");
        jsoupDocument.body().select("tfoot.total>tr").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;page-break-inside: avoid;");
        jsoupDocument.body().select("tfoot.total>tr>td:not([class])").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;border: 1px solid gray;text-align: center;");
        jsoupDocument.body().select("tfoot.total>tr>td.duration").attr("style", "-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 5px;border: 1px solid gray;text-align: right;white-space: nowrap;");

        jsoupDocument.body().select("a").unwrap();
        return jsoupDocument;
    }
}
