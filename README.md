# 基于SpringBoot+JSoup+POI+Swagger2实现校园教务系统成绩课程等信息抓取，并提供接口访问的小项目

#### 介绍
教务系统（强智系统）、图书馆系统常用接口，可查询学生信息、学生课程信息、成绩信息、素拓修学情况、绩点情况、考试时间、图书馆推荐书目、搜索图书。实现了强智教务系统的模拟登录，网页解析，返回json数据格式。



#### 项目地址

| Github                                           | 码云                                     |
| ------------------------------------------------ | ---------------------------------------- |
| https://github.com/waiterxiaoyy/waiter-gdufe-api | https://gitee.com/waiterxiaoyy/gdufe-api |



#### 参考接口文档

> https://docs.apipost.cn/preview/aadd819acb856ef2/66465aa51a4e56f1
>
> 你也可以在启动项目后访问：http://localhost:9093/swagger-ui.html



#### 项目介绍

##### 教务系统登录

解决抓取教务系统信息最主要的就是登录问题，只要获得登录成功后的cookie，然后携带cookie去访问其他页面，再对页面进行解析即可，这里描述模拟登录的过程，首先是获取学号和密码，然后获取验证码，由于验证码是图片，所以需要对图片进行解析，这里采用二值化的方式，获得四位的code，再通过jsoup携带cookie、学号、密码、code模拟登录。

```java
/**
 * @author :WaiterXiaoYY
 * @description: 模拟登录工具类
 * @data :2020/12/17 10:29
 */
public class InitLogin {

    private String url_Login = "http://jwxt.gdufe.edu.cn/jsxsd/xk/LoginToXkLdap"; // 登录
    private String url_safecode = "http://jwxt.gdufe.edu.cn/jsxsd/verifycode.servlet"; // 验证码


    //学号，密码
    private String username;
    private String password;

    private GetSafeCode getSafeCode = new GetSafeCode();
    public Map<String, String> cookie;
    public byte[] bytes;
    

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 读取验证码
     * 保存Cookie
     * @throws IOException
     */
    public void getCookie() throws IOException {
        Connection.Response response = Jsoup.connect(url_safecode).ignoreContentType(true) // 获取图片需设置忽略内容类型
                .userAgent("Mozilla").method(Connection.Method.GET).timeout(3000).execute();
        cookie = response.cookies();
        bytes = response.bodyAsBytes();
    }
    
    
    /**
     * 模拟登录教务系统，得到cookie，方便后续读取各类网页
     */
    public void initLogin() throws IOException {
        String code = getSafeCode.getSafeCode(bytes);
        try {
            Map<String, String> data = new HashMap<String, String>();
            data.put("USERNAME", username);
            data.put("PASSWORD", password);
            data.put("RANDOMCODE", code);
            Connection connect = Jsoup.connect(url_Login)
                    .header("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                    .method(Connection.Method.POST)
                    .data(data)
                    .timeout(3000);

            for (Map.Entry<String, String> entry : cookie.entrySet()) {
                connect.cookie(entry.getKey(), entry.getValue());
            }
            Connection.Response response = connect.execute();
        } catch (IOException e) {

        }
    }

}
```



```java
/**
 * @author :WaiterXiaoYY
 * @description: 获取验证码，并使用一个工具类ImgIdenfy进行解析，得到code
 * @data :2020/12/17 10:29
 */
public class GetSafeCode {

    private String url_safecode = "http://jwxt.gdufe.edu.cn/jsxsd/verifycode.servlet"; // 验证码


    public String getSafeCode(byte[] bytes) throws IOException {
        ImgIdenfy imgIdenfy = new ImgIdenfy();
        //InputStream inputStream = new URL(url_safecode).openStream();

        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage sourceImg = ImageIO.read(inputStream);
        imgIdenfy.writeImage(sourceImg);
        int[][] imgArr = imgIdenfy.binaryImg(sourceImg); // 二值化
        imgIdenfy.removeByLine(imgArr); // 去除干扰先 引用传递
        int[][][] imgArrArr = imgIdenfy.imgCut(imgArr,
                new int[][]{new int[]{4, 13}, new int[]{14, 23}, new int[]{24, 33}, new int[]{34, 43}},
                new int[][]{new int[]{4, 16}, new int[]{4, 16}, new int[]{4, 16}, new int[]{4, 16}},
                4);
        return imgIdenfy.matchCode(imgArrArr);
    }
}
```



```java
/**
 * @author :WaiterXiaoYY
 * @description: 将验证码图片以流的方式读入，然后根据rbg的大小，二值化整个图片，去除干扰线，然后进行匹配，得到code
 * @data :2020/12/17 10:29
 */
public class ImgIdenfy {

    private int height = 22;
    private int width = 62;
    private int rgbThres = 150;
    private String url_safecode = "http://jwxt.gdufe.edu.cn/jsxsd/verifycode.servlet"; // 验证码

    public int[][] binaryImg(BufferedImage img) {
        int[][] imgArr = new int[this.height][this.width];
        for (int x = 0; x < this.width; ++x) {
            for (int y = 0; y < this.height; ++y) {
                if (x == 0 || y == 0 || x == this.width - 1 || y == this.height - 1) {
                    imgArr[y][x] = 1;
                    continue;
                }
                int pixel = img.getRGB(x, y);
                if (((pixel & 0xff0000) >> 16) < this.rgbThres && ((pixel & 0xff00) >> 8) < this.rgbThres && (pixel & 0xff) < this.rgbThres) {
                    imgArr[y][x] = 0;
                } else {
                    imgArr[y][x] = 1;
                }
            }
        }
        return imgArr;
    }

    // 去掉干扰线
    public void removeByLin.e(int[][] imgArr) {
        for (int y = 1; y < this.height - 1; ++y) {
            for (int x = 1; x < this.width - 1; ++x) {
                if (imgArr[y][x] == 0) {
                    int count = imgArr[y][x - 1] + imgArr[y][x + 1] + imgArr[y + 1][x] + imgArr[y - 1][x];
                    if (count > 2) imgArr[y][x] = 1;
                }
            }
        }
    }

    // 裁剪
    public int[][][] imgCut(int[][] imgArr, int[][] xCut, int[][] yCut, int num) {
        int[][][] imgArrArr = new int[num][yCut[0][1] - yCut[0][0]][xCut[0][1] - xCut[0][0]];
        for (int i = 0; i < num; ++i) {
            for (int j = yCut[i][0]; j < yCut[i][1]; ++j) {
                for (int k = xCut[i][0]; k < xCut[i][1]; ++k) {
                    imgArrArr[i][j-yCut[i][0]][k-xCut[i][0]] = imgArr[j][k];
                }
            }
        }
        return imgArrArr;
    }

    // 转字符串
    public String getString(int[][] imgArr){
        StringBuilder s = new StringBuilder();
        int unitHeight = imgArr.length;
        int unitWidth = imgArr[0].length;
        for (int y = 0; y < unitHeight; ++y) {
            for (int x = 0; x < unitWidth; ++x) {
                s.append(imgArr[y][x]);
            }
        }
        return s.toString();
    }

    // 相同大小直接对比
    private int comparedText(String s1,String s2){
        int n = s1.length();
        int percent = 0;
        for(int i = 0; i < n ; ++i) {
            if (s1.charAt(i) == s2.charAt(i)) percent++;
        }
        return percent;
    }

    /**
     * 匹配识别
     * @param imgArrArr
     * @return
     */
    public String matchCode(int [][][] imgArrArr){
        StringBuilder s = new StringBuilder();
        Map<String,String> charMap = CharMap.getCharMap();
        for (int[][] imgArr : imgArrArr){
            int maxMatch = 0;
            String tempRecord = "";
            for(Map.Entry<String,String> m : charMap.entrySet()){
                int percent = this.comparedText(this.getString(imgArr),m.getValue());
                if(percent > maxMatch){
                    maxMatch = percent;
                    tempRecord = m.getKey();
                }
            }
            s.append(tempRecord);
        }
        return s.toString();
    }

    // 写入硬盘
    public void writeImage(BufferedImage sourceImg) {
        File imageFile = new File("v.jpg");
        try {
            FileOutputStream outStream = new FileOutputStream(imageFile);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(sourceImg, "jpg", out);
            byte[] data = out.toByteArray();
            outStream.write(data);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    // 控制台打印
    public void showImg(int[][] imgArr) {
        int unitHeight = imgArr.length;
        int unitWidth = imgArr[0].length;
        for (int y = 0; y < unitHeight; ++y) {
            for (int x = 0; x < unitWidth; ++x) {
                System.out.print(imgArr[y][x]);
            }
            System.out.println();
        }
    }
}

```



##### Jsoup解析网页

> jsoup 是一个用于处理真实世界 HTML 的 Java 库。它使用最好的 HTML5 DOM 方法和 CSS 选择器提供了一个非常方便的 API，用于获取 URL 以及提取和操作数据。具体学习可以参考https://jsoup.org/

此项目中几乎每个网页解析都用到了jsoup，jsoup是比较简单的，这里描述一个页面解析的过程，其他的类似。

```java
/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/17 20:03
 */

public class SelectiveCourseJsoup {
    

    public static List<HaveStudied> getHaveStudied(String studentId, String password) throws IOException {
        Map<String, HaveStudied> haveStudiedMap = new HashMap<String, HaveStudied>();
        Map<String, String> data = new HashMap<String, String>(); // 此map是用户请求体中的key-value对应，具体参数设置参考浏览器请求过程携带的参数

        // 模拟登录，获取cookie
        InitLogin initLogin = new InitLogin();
        initLogin.setUsername(studentId);
        initLogin.setPassword(password);
        initLogin.getCookie();
        initLogin.initLogin();

        double gpaUp = 0;
        double creditTotal = 0;


        //设置请求体参数
        data.put("kksj", "");
        data.put("kcxz", "");
        data.put("kcmc", "");
        data.put("fxkc", "0");
        data.put("xsfs", "all");
        
        // jsoup连接，携带map和cookie，使用post请求
        Connection connection = Jsoup.connect("http://jwxt.gdufe.edu.cn/jsxsd/kscj/cjcx_list")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.POST)
                .data(data)
                .timeout(3000)
                .cookies(initLogin.cookie);
        Connection.Response response = connection.execute();
        
        // 获得一个html文档
        Document document = response.parse();

        //解析文档
        //Document document = Jsoup.parse(file, "UTF-8", "");

        // 由于本接口是课程选修情况，由于选修课有各种类型，而课程所属类型在教务系统中未显示出来，需要调用由教务处提供的选修课参考excel，利用poi解析此excel，然后对应到课程的类型，poi在下面进行描述
        Map<String, SelectiveCourse> selectiveCourseMap = SelectiveCoursePoi.getSelectiveCourse();

        // 解析文档，参考jsoup网
        Element table = document.getElementById("dataList");
        Elements trs = table.getElementsByTag("tr");
        Grade grade = null;
        for (int i = 0; i < trs.size(); i++) {
            if (i > 0) {
                Element tr = trs.get(i);
                Elements tds = tr.getElementsByTag("td");
                grade = new Grade();
                grade.setStudentid(studentId);
                for (int j = 1; j < 15; j++) {
                    Element td = tds.get(j);
                    if (j == 1) {
                        grade.setTerm(td.text());
                    }
                    if (j == 2) {
                        grade.setCourseid(td.text());
                    }
                    if (j == 3) {
                        grade.setCourse(td.text());
                    }
                    if (j == 4) {
                        if (td.text().equals("")) {
                            grade.setRegularScore(0);
                        } else {
                            grade.setRegularScore(Double.parseDouble(td.text()));
                        }
                    }
                    if (j == 5) {
                        if (td.text().equals("")) {
                            grade.setExperimentScore(0);
                        } else {
                            grade.setExperimentScore(Double.parseDouble(td.text()));
                        }
                    }
                    if (j == 6) {
                        if (td.text().equals("")) {
                            grade.setFinalScore(0);
                        } else {
                            grade.setFinalScore(Double.parseDouble(td.text()));
                        }
                    }
                    if (j == 7) {
                        if (td.text().equals("")) {
                            grade.setTotalmark(0);
                        } else if (td.text().equals("优")) {
                            grade.setTotalmark(95);
                        } else if (td.text().equals("良")) {
                            grade.setTotalmark(85);
                        } else if (td.text().equals("中")) {
                            grade.setTotalmark(75);
                        } else if (td.text().equals("及格")) {
                            grade.setTotalmark(65);
                        } else if (td.text().equals("不及格")) {
                            grade.setTotalmark(0);
                        } else {
                            grade.setTotalmark(Double.parseDouble(td.text()));
                        }
                    }
                    if (j == 8) {
                        grade.setCredit(Double.parseDouble(td.text()));
                    }
                    if (j == 11) {
                        grade.setCoursecategory(td.text());
                    }
                    if (j == 12) {
                        grade.setCourseNature(td.text());
                    }
                    if (j == 14) {
                        grade.setExamNature(td.text());
                    }
                }
                gpaUp += (grade.getTotalmark() - 50) / 10 * grade.getCredit();

                String scoreType = grade.getCoursecategory() + grade.getCourseNature();

                String coursetype = "";
                if (scoreType.equals("选修通识课")) {
                    if (selectiveCourseMap.get(grade.getCourse()) == null) {
                        coursetype = "选修通识课-未知模块";
                    } else
                        coursetype = "选修通识课-" + selectiveCourseMap.get(grade.getCourse()).getCoursetype();
                } else {
                    coursetype = scoreType;
                }


                if (haveStudiedMap.containsKey(coursetype)) {
                    HaveStudied haveStudied = haveStudiedMap.get(coursetype);
                    List<Grade> scoreList = haveStudied.getScoreList();
                    scoreList.add(grade);
                    haveStudied.setScoreList(scoreList);
                    haveStudied.setTotal(haveStudied.getTotal() + grade.getCredit());
                    haveStudiedMap.put(coursetype, haveStudied);
                } else {
                    HaveStudied haveStudied = new HaveStudied();
                    haveStudied.setCoursetype(coursetype);
                    List<Grade> scoreList = new ArrayList<Grade>();
                    scoreList.add(grade);
                    haveStudied.setScoreList(scoreList);
                    haveStudied.setTotal(grade.getCredit());
                    haveStudiedMap.put(coursetype, haveStudied);
                }
            }
        }
        List<HaveStudied> haveStudiedList = new ArrayList<>();
        for (HaveStudied haveStudied : haveStudiedMap.values()) {
            haveStudiedList.add(haveStudied);
        }
        return haveStudiedList;
    }

	//提取数字
    public static String toNum(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    
}

```



##### POI操作

 [Apache](https://so.csdn.net/so/search?q=Apache&spm=1001.2101.3001.7020) POI是Apache软件基金会的开源项目，POI提供API给Java程序对Microsoft Office格式档案读和写的功能。 .NET的开发人员则可以利用NPOI (POI for .NET) 来存取 Microsoft Office文档的功能。

**一、结构：**

- HSSF － 提供读写Microsoft Excel格式档案的功能。

- XSSF － 提供读写Microsoft Excel OOXML格式档案的功能。
- HWPF － 提供读写Microsoft Word格式档案的功能。
- HSLF － 提供读写Microsoft PowerPoint格式档案的功能。
- HDGF － 提供读写Microsoft Visio格式档案的功能。

**二、HSSF概况**

　　HSSF 是Horrible SpreadSheet Format的缩写，通过HSSF，你可以用纯Java代码来读取、写入、修改Excel文件。HSSF 为读取操作提供了两类API：usermodel和eventusermodel，即“用户模型”和“事件-用户模型”。

**三、 POI EXCEL文档结构类**

- HSSFWorkbook excel文档对象
- HSSFSheet excel的sheet
- HSSFRow excel的行
- HSSFCell excel的单元格
- HSSFFont excel字体
- HSSFName 名称
- HSSFDataFormat 日期格式
- HSSFHeader sheet头
- HSSFFooter sheet尾
- HSSFCellStyle cell样式
- HSSFDateUtil 日期
- HSSFPrintSetup 打印
- HSSFErrorConstants 错误信息表

```java

/**
 * @author :WaiterXiaoYY
 * @description: 读取选修课
 * @data :2020/12/17 19:40
 */
public class SelectiveCoursePoi {
    private static HashMap<String, SelectiveCourse> selectiveCourseMap;
    private static String filepath = "src\\main\\resources\\static\\List of elective courses.xlsx";
    public static HashMap<String, SelectiveCourse> getSelectiveCourse() {
        selectiveCourseMap = new HashMap<String, SelectiveCourse>();

        try {
            InputStream inputStream = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int maxRowNum = sheet.getPhysicalNumberOfRows();

            for(int i = 0; i < maxRowNum; i++) {
                Row row = sheet.getRow(i);

                String courseid = getCellStringValue(row.getCell(1));
                String coursename = getCellStringValue(row.getCell(2));
                Integer credit = Integer.parseInt(getCellStringValue(row.getCell(3)));
                String courseCollege = getCellStringValue(row.getCell(4));
                String coursetype = getCellStringValue(row.getCell(5));
                if(selectiveCourseMap.containsKey(coursename)) {
                    SelectiveCourse selectiveCourse = selectiveCourseMap.get(coursename);
                    List<String> courseidList = selectiveCourse.getCourseid();
                    courseidList.add(courseid);
                    selectiveCourse.setCourseid(courseidList);

                    List<Integer> creditList = selectiveCourse.getCredit();
                    creditList.add(credit);
                    selectiveCourse.setCredit(creditList);

                    selectiveCourseMap.put(coursename, selectiveCourse);
                } else {
                    List<String> courseidList1 = new ArrayList<String>();
                    List<Integer> creditList1 = new ArrayList<Integer>();
                    courseidList1.add(courseid);
                    creditList1.add(credit);

                    // SelectiveCourse是对应的实体类
                    SelectiveCourse selectiveCourse = new SelectiveCourse(coursetype, courseidList1, coursename, creditList1, courseCollege);
                    selectiveCourseMap.put(coursename, selectiveCourse);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selectiveCourseMap;
    }
   
    // 通用方法，处理excel单元格格式
    public static String getCellStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        int type = cell.getCellType();
        String cellValue;
        switch (type) {
            case 3:
                cellValue = "";
                break;
            case 5  :
                cellValue = "";
                break;
            case 4:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case 0:
                cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                break;
            case 1:
                cellValue = cell.getStringCellValue();
                break;
            case 2:
                cellValue = cell.getCellFormula();
                break;
            default:
                cellValue = "";
                break;
        }
        return cellValue;
    }
}

```



##### 配置文件

- 跨域请求

```java
/**
 * @author     ：WaiterXiaoYY
 * @description：配置跨域请求
 */

@Configuration
public class                            CorsConfig implements WebMvcConfigurer {
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
```

- swagger-ui配置

```java
/**
 * @author     ：WaiterXiaoYY
 * @description：配置Swagger
 */

@Configuration //配置类
@EnableSwagger2// 开启Swagger2的自动配置
public class SwaggerConfig {

    @Bean //配置docket以配置Swagger具体参数
    public Docket docket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())        // 将文档信息注入
                .groupName("waiterxiaoyy") // 分组名
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.waiterxiaoyy.backandroiddesign.controller")) // 扫描接口路径
                .build();
    }

    //配置文档信息
    private ApiInfo apiInfo() {
        Contact contact = new Contact("WaiterXiaoYY", "http://waiterxiaoyy.ltd/", "waiterxiaoyy@qq.com");
        return new ApiInfo(
                "接口文档", // 标题
                "[状态码：200-请求成功（返回数据） // 202-请求获取失败（捕获异常返回）]" , // 描述
                "v1.0", // 版本
                "", // 组织链接
                contact, // 联系人信息
                "", // 许可
                "", // 许可连接
                new ArrayList()// 扩展
        );
    }
}
```



#### 接口样式

##### 获取学生课程

##### 接口URL

> http://localhost:9093/api/getstucourse



##### 请求方式

> POST



##### Content-Type

> json



##### 请求Body参数

```javascript
{
  "password": "xxx",
  "studentId": "1825xxxxxxxx",
  "termTime": "2021-2022-1"
}
```

| 参数名    | 示例值      | 参数类型 | 参数描述                  |
| --------- | ----------- | -------- | ------------------------- |
| password  | xxx         | Text     | 教务系统密码              |
| studentId | 1825xxxxxxx | Text     | 学号                      |
| termTime  | 2021-2022-1 | Text     | 学期（格式：2021-2022-1） |



##### 成功响应示例

```javascript
{
	"code": 200,
	"msg": "获取学生课程成功",
	"count": 1,
	"data": [
		{
			"studentid": "1825xxxxxxx",
			"coureseid": "2bf105e3-3",
			"course": "应用软件系统综合设计",
			"teacher": "胡建军副教授",
			"week": [
				"9-16(周)",
				"9-16(周)",
				"9-16(周)",
				"9-16(周)",
				"9-16(周)",
				"9-16(周)",
				"9-16(周)",
				"9-16(周)",
				"9-16(周)"
			],
			"position": [
				"实验楼803（软件工程第一实验室）",
				"实验楼803（软件工程第一实验室）",
				"实验楼803（软件工程第一实验室）",
				"实验楼803（软件工程第一实验室）",
				"实验楼803（软件工程第一实验室）",
				"实验楼803（软件工程第一实验室）",
				"实验楼803（软件工程第一实验室）",
				"实验楼803（软件工程第一实验室）",
				"实验楼803（软件工程第一实验室）"
			],
			"count": [
				"[01-02]节",
				"[03-04]节",
				"[05-06]节",
				"[01-02]节",
				"[03-04]节",
				"[05-06]节",
				"[01-02]节",
				"[03-04]节",
				"[05-06]节"
			],
			"weekCountPositon": [
				"9-16(周)[01-02]节实验楼803（软件工程第一实验室）",
				"9-16(周)[03-04]节实验楼803（软件工程第一实验室）",
				"9-16(周)[05-06]节实验楼803（软件工程第一实验室）"
			]
		}
	]
}
```

| 参数名                | 示例值                                           | 参数类型 | 参数描述                                   |
| --------------------- | ------------------------------------------------ | -------- | ------------------------------------------ |
| code                  | 200                                              | Text     |                                            |
| msg                   | 获取学生课程成功                                 | Text     | 返回信息描述                               |
| count                 | 1                                                | Text     |                                            |
| data                  |                                                  | Text     | 返回数据                                   |
| data.studentid        | 1825xxxxxxx                                      | Text     | 学号                                       |
| data.coureseid        | 2bf105e3-3                                       | Text     | 课程代号（后台随机生成，不是真正的课程号） |
| data.course           | 应用软件系统综合设计                             | Text     | 课程名称                                   |
| data.teacher          | 胡建军副教授                                     | Text     | 老师名称                                   |
| data.week             | 9-16(周)                                         | Text     | 上课周期                                   |
| data.position         | 实验楼803（软件工程第一实验室）                  | Text     | 上课地点                                   |
| data.count            | [01-02]节                                        | Text     | 上课时间                                   |
| data.weekCountPositon | 9-16(周)[01-02]节实验楼803（软件工程第一实验室） | Text     | 上课周期时间地点                           |
|                       |                                                  |          |                                            |



##### 失败响应示例

```javascript
{
	"code": 202,
	"msg": "获取学生课程失败",
	"count": null,
	"data": null
}
```

| 参数名 | 示例值           | 参数类型 | 参数描述     |
| ------ | ---------------- | -------- | ------------ |
| code   | 202              | Text     |              |
| msg    | 获取学生课程失败 | Text     | 返回文字描述 |
| count  |                  | Text     |              |
| data   |                  | Text     | 返回数据     |

