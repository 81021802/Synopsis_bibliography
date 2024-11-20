package com.example.gujitiyao.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.example.gujitiyao.entity.*;
import com.example.gujitiyao.service.DataListener;
import com.example.gujitiyao.service.DataService;
import com.example.gujitiyao.service.UserService;
import com.example.gujitiyao.utils.Result;
import org.elasticsearch.index.query.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class GujiController {

    /*// http:/localhost:8080/hello
    @GetMapping("/hello")
    public String hello(){
        return "hello world——你好";
    }*/
    @Autowired
    private UserService userService;

    @Autowired
    private DataService dataService;

//    @PostMapping("/save")
//    public String save(){
//        Book book = new Book();
//        book.setId(11201840L);
//        book.setBookName("郡斋读书志");
//        book.setBookAuthor("[宋]晁公武");
//        book.setTitle("张师黯集五十卷");
//        book.setLocation("后志卷二");
//        book.setMainTitle("张师黯集");
//        book.setVolumes("五十卷");
//        book.setParallelTitle("");
//        book.setPart("");
//        book.setCategory("别集类");
//        book.setEdition("");
//        book.setEditionDynasty("");
//        book.setEditionType("");
//        book.setAuthor("");
//        book.setAuthor1Dynasty("");
//        book.setAuthor1("");
//        book.setResponsibility1("");
//        book.setAuthor2("");
//        book.setAuthor2Dynasty("");
//        book.setResponsibility2("");
//        book.setAuthor3("");
//        book.setAuthor3Dynasty("");
//        book.setResponsibility3("");
//        book.setSummary("右皇朝张洎字师黯，滁州人。仕李煜，知制诰、中书舍人。归朝，为史馆修撰、翰林学士。淳化中，参知政事。至道二年，卒。洎风神洒落，文辞清丽，通释氏学，然性险诐而谄附。集有呉淑序，其子安期所编，咸平五年上之。");
//        userService.save(book);
//        return ("新增成功！");
//    }

/*
    @PostMapping("/saveAll")
    public String saveAll(){
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(23145L,"持靜齋藏書記要","莫友芝","珊瑚鉤詩話三卷","張表臣","集部","","宋張表臣撰。舊鈔本。"));
        bookList.add(new Book(11100035684L,"持靜齋藏書記要","莫友芝","酌中志餘一厚册","","史部","雜史","鈔本。不題編人。其自識云：『編酌中志既竣，篋中有昌、啓、禎三朝紀載之堪與兹志發明者，曰東林點將録、王紹徽。曰東林朋黨録、曰東林同志録、曰東林籍貫、曰盜柄東林夥、曰天鑒錄、上五種未詳撰人。曰夥壞封疆録、昭陽魏應嘉。曰欽定逆案、曰天啓宫詞、虞山陳。曰擬故宫詞，毘陵唐字昭。凡十種合編之，而題以志餘。』然則編者亦劉若愚也。其前七種俱見四庫『傳記①類』存目，而志餘不收。①傳記，原目誤乙，據四庫全書總目改。"));
        bookList.add(new Book(1L,"四库全书总目提要","纪昀总纂","覆瓿草六卷","林烃","集部三一","别集类存目五","明林烃撰。烃字贞耀，闽县人。嘉靖壬戌进士，官至南京工部尚书。事迹附见《明史·林瀚传》集。首有王稚登序，言“烃官未踰藩镇，既告归，几二十年，乃以荐为太仆，俄复请归。”与史不合，盖史举其所终之官，稚登所称则其刻集时官也。稚登序极论七子末流之弊，而独称烃诗为有道之言。然是集为胡应麟所编，应麟故依附七子者。集中所录，大抵旧调居多，新意殊少，仍七子之支派而已。"));
        bookList.add(new Book(235L,"四库全书总目提要","纪昀总纂","槜李往哲前编","戚元佐","史部一七","传记类存目三","明戚元佐撰。元佐字希仲，嘉兴人。嘉靖壬戌进士，官至尚宝司卿。是编取洪武至历初年嘉兴前哲自程本立以下共十四人，各为一传。王世贞为之序，其称《前编》者，则以国朝项玉笋有《续编》之刻，追题此名也。"));
        bookList.add(new Book(564L,"直齋書錄解題","陳振孫","姓源韻譜一卷","張九齡","","典故類","唐張九齡撰。依春秋正典、栁氏萬姓錄、世本圖捃摭諸書，纂為此譜，分四聲以便尋閲。古者賜姓别之，黄帝之子得姓者十四人是也。後世賜姓合之，漢髙帝命婁敬、項伯為劉氏是也。惟其别之也則離析，故古者論姓氏推其本同。惟其合之也則亂，故後世論姓氏識其本異。自五胡亂華，百宗蕩析，夷夏之裔與夫冠冕輿臺之子孫混為一區，不可遽知，此周齊以来譜牒之學所以貴於世也歟。"));
        userService.saveAll(bookList);
        return ("新增成功！");
    }
*/
//    @PostMapping("/saveAll")
//    public String saveAll(){
//        String fileName = "src/main/resources/entity_data2.xlsx";
//        EasyExcel.read(fileName, Entity.class,new DataListener(dataService)).sheet().doRead();
//        return ("新增成功！");
//    }

//    @PostMapping("/updata")
//    public String updata(Book book){
//        userService.save(book);
//        return ("修改成功！");
//    }
    @GetMapping("/entity/{textID}")
    public List<Entity> getByTextID(@PathVariable Long textID){
        return userService.getByTextID(textID);
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable("id") Long id){
        return userService.getById(id);
    }

    @PostMapping("/groupByEditionDynasty")
    public List<Map.Entry<String, Long>> groupByEditionDynasty(){
        return userService.groupByEditionDynasty();
    }

    @PostMapping("/groupByFigureDynasty")
    public List<Map.Entry<String, Long>> groupByFigureDynasty(){
        return userService.groupByFigureDynasty();
    }

    @PostMapping("/groupByBookName")
    public List<Map.Entry<String, Long>> groupByBookName(){
        return userService.groupByBookName();
    }

    @PostMapping("/findByBookName")
    public List<Map.Entry<String, Long>> findByBookName(@RequestBody bookRequest bookRequest){
        String bookName = bookRequest.getBookName();
        return userService.findByBookName(bookName);
    }

    @PostMapping("/findByBookNamePart")
    public List<Map.Entry<String, Long>> findByBookNamePart(@RequestBody bookRequest bookRequest){
        String bookName = bookRequest.getBookName();
        String part = bookRequest.getPart();
        return userService.findByBookNamePart(bookName,part);
    }

    @PostMapping("/getTop50FiguresByCount")
    public List<Figure> getTop50FiguresByCount(){
        return userService.getTop50FiguresByCount();
    }

//    @DeleteMapping("/deleteAll")
//    public String deleteAll(){
//        userService.deleteAll();
//        return ("已全部删除");
//    }

//    @DeleteMapping("/{id}")
//    public String deleteById(@PathVariable("id") Long id){
//        userService.deleteById(id);
//        return ("删除成功！");
//    }

//    @GetMapping("/all")
//    public List<Book> getAll(){
//        return userService.getAll();
//    }

    @PostMapping("/queryStringQuery")
    /*public List<Book> queryStringQuery(@RequestBody String jsonData){
        JSONObject jsonObject  = JSONObject.parseObject(jsonData);
        JSONArray fieldsArray = jsonObject.getJSONArray("fields");
        Map<String,Float> fields = fieldsArray.stream().collect(Collectors.toMap(object ->{
            JSONObject item = (JSONObject) object;
            return item.getString("field");
        },object ->{
            JSONObject item = (JSONObject) object;
            return item.getFloat("score");
        }));
        String queryString = jsonObject.getString("queryString");
        String analyzer = jsonObject.getString("analyzer");
        String operator = jsonObject.getString("operator");
        if (operator == "and"){
            return userService.queryStringQuery(fields,queryString,analyzer,Operator.AND,Book.class);
        }
        else {
            return userService.queryStringQuery(fields,queryString,analyzer,Operator.OR,Book.class);
        }

    }*/
    public List<Book> queryStringQuery(@RequestBody userQuery query){
        Map<String,Float> fields = query.getFields().stream().collect(Collectors.toMap(p->p.getField(),p->p.getScore()));
        if (query.getOperator().equals("and")){
            return userService.queryStringQuery(fields,query.getQueryString(),query.getAnalyzer(),Operator.AND,Book.class);
        }
        else {
            return userService.queryStringQuery(fields,query.getQueryString(),query.getAnalyzer(),Operator.OR,Book.class);
        }
    }

    @PostMapping("/boolQueryBuildByMust")
    //Class<T> classType
    public Result boolQueryBuildByMust(@RequestBody searchQuery searchQuery){
            Map<String, String> fields = searchQuery.toMap();
        return userService.boolQueryBuildByMust(fields,searchQuery.getPageNum(),searchQuery.getPageSize(),searchQuery.getPreTags(),searchQuery.getPostTags(),Book.class);
    }

    @PostMapping("/boolQueryBuildByMustNot")
    public <T> List<T> boolQueryBuildByMustNot(Map<String,String[]> fields, Class<T> classType){
        return userService.boolQueryBuildByMustNot(fields,classType);
    }

    @PostMapping("/boolQueryBuildByShould")
    public Result boolQueryBuildByShould(@RequestBody searchQuery searchQuery){
        Map<String, String> fields = searchQuery.toMap();
        return userService.boolQueryBuildByShould(fields,searchQuery.getPageNum(),searchQuery.getPageSize(),searchQuery.getPreTags(),searchQuery.getPostTags(),Book.class);
    }

    @PostMapping("/highlightBuilder")
    public List<Book> highlightBuilder(@RequestBody JSONObject jsonObject){
        String field = jsonObject.getString("field");
        String preTags = jsonObject.getString("preTags");
        String postTags = jsonObject.getString("postTags");
        String text = jsonObject.getString("text");
        return userService.highlightBuilder(field,preTags,postTags,text,Book.class);
    }

    @PostMapping("/termSuggestion")
    public List<String> termSuggestion(String fieldName,String text,Class<?> classType){
        return userService.termSuggestion(fieldName,text,classType);
    }

    @PostMapping("/wildcardQuery")
    public <T> List<T> wildcardQuery(String key,String value,Class<T> classType){
        return userService.wildcardQuery(key,value,classType);
    }

    @PostMapping("/selectFindPage")
    public List<Book> selectFindPage(String key,Integer pageNum,Integer pageSize){
        return userService.selectFindPage(key,pageNum,pageSize,Book.class);
    }

    @PostMapping("/multiMatchQuery")
    public List<Book> multiMatchQuery(String keyword,String[] fields){
        return userService.multiMatchQuery(keyword,fields,Book.class);
    }

    @PostMapping("/multiMatchQueryPage")
//    public List<Book> multiMatchQueryPage(@RequestParam("keyword") String keyword,
//                                          @RequestParam("fields") String[] fields,
//                                          @RequestParam("pageNum") Integer pageNum,
//                                          @RequestParam("pagesize") Integer pageSize)
    public Result multiMatchQueryPage(String keyword, Integer pageNum, Integer pageSize,String preTags,String postTags){
        return userService.multiMatchQueryPage(keyword,pageNum,pageSize,preTags,postTags,Book.class);
    }
}
