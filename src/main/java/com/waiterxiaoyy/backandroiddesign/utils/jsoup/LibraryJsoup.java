package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import com.waiterxiaoyy.backandroiddesign.entity.BookSearch;
import com.waiterxiaoyy.backandroiddesign.entity.RecommendBook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/7 16:10
 */
public class LibraryJsoup {
    private static String libraryUrl = "http://opac.library.gdufe.edu.cn/top/top_lend.php";
    public static List<RecommendBook> getRecommendBook () {
        List<RecommendBook> libraryJsoupList = null;
        try {
            Connection connection = Jsoup.connect(libraryUrl);
            Connection.Response response = connection.execute();
            Document document = response.parse();
//        System.out.println(document);

            Elements table = document.select("table");

            Elements trs = table.select("tr");
            libraryJsoupList = new ArrayList<RecommendBook>();
            for(int i = 1; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td");
                RecommendBook recommendBook = new RecommendBook();
                for(int j = 0; j < tds.size(); j++) {
                    if(j == 1) {
                        String bookHref = tds.get(j).select("a").first().attr("href").toString();
                        String bookName = tds.get(j).select("a").text();
                        recommendBook.setBookid(bookHref.substring(bookHref.indexOf("=") + 1));
                        recommendBook.setBookname(bookName);
                    }
                    if ( j == 2) {
                        recommendBook.setAuthor(tds.get(j).text());
                    }
                    if (j == 3) {
                        recommendBook.setPublishHouse(tds.get(j).text());
                    }
                    if (j == 4) {
                        recommendBook.setCallNum(tds.get(j).text());
                    }
                    if (j == 5) {
                        recommendBook.setCollection(Integer.parseInt(tds.get(j).text()));
                    }
                    if (j == 6) {
                        recommendBook.setBorrowNum(Integer.parseInt(tds.get(j).text()));
                    }
                    if(j == 7) {
                        recommendBook.setBorrowRatio(Double.parseDouble(tds.get(j).text()));
                    }
                }
                libraryJsoupList.add(recommendBook);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return libraryJsoupList;
    }

    public static List<BookSearch> getSearchBookList(String query) throws IOException {
        Connection connection = Jsoup.connect("http://opac.library.gdufe.edu.cn/opac/openlink.php?strSearchType=title&match_flag=forward&historyCount=1&strText=" + query+ "&doctype==ALL&with_ebook=on&displaypg=20&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL");
        Connection.Response response = connection.execute();
        Document document = response.parse();

        Element element = document.getElementById("search_book_list");
        List<BookSearch> bookSearchList = new ArrayList<>();
        if(element != null) {
            Elements bookLi = element.getElementsByAttributeValue("class", "book_list_info");
            for(int i = 0; i < bookLi.size(); i++) {


                String bookListInfo[] = bookLi.get(i).text().split(" ");
                BookSearch bookSearch = new BookSearch();
                String bookName = bookListInfo[0].split("\\.")[1];
                bookSearch.setBookName(bookName);
                bookSearch.setBookISBN(bookListInfo[1]);
                bookSearch.setCollection(bookListInfo[2]);
                bookSearch.setBorrowNum(bookListInfo[3]);



                int firstIndex = bookLi.get(i).toString().indexOf("</span>", bookLi.get(i).toString().indexOf("</span>") + 4);
                int endIndex = bookLi.get(i).toString().indexOf("<br>", firstIndex);
                String author = bookLi.get(i).toString().substring(firstIndex+7, endIndex).trim();
                int imagIndex = bookLi.get(i).toString().indexOf("<img", endIndex);
                String publicHouse = bookLi.get(i).toString().substring(endIndex+4, imagIndex-5).replaceAll("&nbsp;", "").trim();
                bookSearch.setAuthor(author);
                bookSearch.setPublishHouse(publicHouse);
                bookSearchList.add(bookSearch);
            }
        }
        return bookSearchList;
    }
    public static void main(String[] args) throws IOException {
        Connection connection = Jsoup.connect("http://opac.library.gdufe.edu.cn/top/top_lend.php");
        Connection.Response response = connection.execute();
        Document document = response.parse();
//        System.out.println(document);

        Elements table = document.select("table");

        Elements trs = table.select("tr");
        List<RecommendBook> libraryJsoupList = new ArrayList<RecommendBook>();
        for(int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            RecommendBook recommendBook = new RecommendBook();
            for(int j = 0; j < tds.size(); j++) {
                if(j == 1) {
                    String bookHref = tds.get(j).select("a").first().attr("href").toString();
                    String bookName = tds.get(j).select("a").text();
                    recommendBook.setBookid(bookHref.substring(bookHref.indexOf("=") + 1));
                    recommendBook.setBookname(bookName);
                }
                if ( j == 2) {
                    recommendBook.setAuthor(tds.get(j).text());
                }
                if (j == 3) {
                    recommendBook.setPublishHouse(tds.get(j).text());
                }
                if (j == 4) {
                    recommendBook.setCallNum(tds.get(j).text());
                }
                if (j == 5) {
                    recommendBook.setCollection(Integer.parseInt(tds.get(j).text()));
                }
                if (j == 6) {
                    recommendBook.setBorrowNum(Integer.parseInt(tds.get(j).text()));
                }
                if(j == 7) {
                    recommendBook.setBorrowRatio(Double.parseDouble(tds.get(j).text()));
                }
            }
            libraryJsoupList.add(recommendBook);
        }
    }
}
