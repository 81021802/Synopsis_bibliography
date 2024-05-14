package com.example.gujitiyao.service.impl;

import com.example.gujitiyao.dao.UserRepository;
import com.example.gujitiyao.entity.Book;
import com.example.gujitiyao.entity.Figure;
import com.example.gujitiyao.service.UserService;
import com.example.gujitiyao.utils.Result;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SortBy;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.clients.elasticsearch7.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;


import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Book save(Book book){
        return userRepository.save(book);
    }

    @Override
    public void saveAll(List<Book> bookList){
        userRepository.saveAll(bookList);
    }

    @Override
    public Book getById(Long id){
        Optional<Book> find = userRepository.findById(id);
        if (find.isPresent()) {
            return find.get();
        }
        return null;
    }

    @Override
    public List<Map.Entry<String, Long>> groupByEditionDynasty(){
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("group_by_edition").field("editionDynasty.keyword").size(50);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withAggregations(aggregation)
                .withPageable(org.springframework.data.domain.PageRequest.of(0, 1))
                .build();

        // 执行查询
        SearchHits<Book> searchHits = elasticsearchRestTemplate.search(searchQuery, Book.class, IndexCoordinates.of("book"));

        // 处理聚合结果
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
        Terms classification = aggregations.aggregations().get("group_by_edition");

        // 预定义的朝代顺序
        List<String> dynastyOrder = Arrays.asList("秦","汉","三国","晋", "魏", "隋", "唐","五代", "金", "宋", "元", "明", "清", "民国");
        // 将搜索命中的文档转换为目标类的实例列表并进行排序
        List<Map.Entry<String, Long>> sortedResults = classification.getBuckets().stream()
                .map(bucket -> new AbstractMap.SimpleEntry<>(bucket.getKeyAsString(), bucket.getDocCount()))
                .sorted(Comparator.comparingInt(entry -> dynastyOrder.indexOf(entry.getKey())))
                .collect(Collectors.toList());

        sortedResults.forEach(entry -> System.out.println("EditionDynasty: " + entry.getKey() + ", Count: " + entry.getValue()));

        return sortedResults;
    }

    @Override
    public List<Map.Entry<String, Long>> groupByBookName(){
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("group_by_field").field("bookName.keyword").size(50);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withAggregations(aggregation)
                .withPageable(org.springframework.data.domain.PageRequest.of(0, 1))
                .build();

        // 执行查询
        SearchHits<Book> searchHits = elasticsearchRestTemplate.search(searchQuery, Book.class, IndexCoordinates.of("book"));

        // 处理聚合结果
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
        Terms classification = aggregations.aggregations().get("group_by_field");
        for (Terms.Bucket bucket : classification.getBuckets()) {
            System.out.println("BookName: " + bucket.getKeyAsString() + ", Count: " + bucket.getDocCount());
        }
        // 将搜索命中的文档转换为目标类的实例列表
        return classification.getBuckets().stream()
                .map(bucket -> new AbstractMap.SimpleEntry<>(bucket.getKeyAsString(), bucket.getDocCount()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map.Entry<String, Long>> groupByFigureDynasty(){
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("group_by_dynasty").field("dynasty.keyword").size(50);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withAggregations(aggregation)
                .withPageable(org.springframework.data.domain.PageRequest.of(0, 1))
                .build();

        // 执行查询
        SearchHits<Figure> searchHits = elasticsearchRestTemplate.search(searchQuery, Figure.class, IndexCoordinates.of("figure"));

        // 处理聚合结果
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
        Terms classification = aggregations.aggregations().get("group_by_dynasty");

        // 预定义的朝代顺序
        List<String> dynastyOrder = Arrays.asList("周","春秋","战国","秦","汉","建安","三国","蜀","晋","南齐","梁","北齐", "魏",
                "隋", "唐","五代","南唐","宋","辽","金",  "元", "明", "清", "民国","近人");
        // 将搜索命中的文档转换为目标类的实例列表并进行排序
        List<Map.Entry<String, Long>> sortedResults = classification.getBuckets().stream()
                .map(bucket -> new AbstractMap.SimpleEntry<>(bucket.getKeyAsString(), bucket.getDocCount()))
                .sorted(Comparator.comparingInt(entry -> dynastyOrder.indexOf(entry.getKey())))
                .collect(Collectors.toList());

        sortedResults.forEach(entry -> System.out.println("EditionDynasty: " + entry.getKey() + ", Count: " + entry.getValue()));

        return sortedResults;
    }

    @Override
    public List<Map.Entry<String, Long>> findByBookName(String bookName){
        /*List<T> results = new ArrayList<>();
        try{
            System.out.println(bookName);
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.termQuery("bookName.keyword", bookName));
            //搜索
            SearchRequest searchRequest = new SearchRequest("book");
            searchRequest.source(builder);

            SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
            for (org.elasticsearch.search.SearchHit hit : searchResponse.getHits()) {
                T result = JSON.parseObject(hit.getSourceAsString(), classType);
                results.add(result);
            }
        } catch (
        IOException e) {
            e.printStackTrace();
        // Consider proper exception handling strategy (logging, rethrowing, etc.)
    }

        return results;*/

        /*Map<String, Object> mapping = elasticsearchRestTemplate.indexOps(IndexCoordinates.of("book")).getMapping();

        if (mapping != null) {
            Map<String, Object> properties = (Map<String, Object>) mapping.get("properties");
            if (properties != null && properties.containsKey("bookName")) {
                Map<String, Object> bookNameFields = (Map<String, Object>) ((Map<String, Object>) properties.get("bookName")).get("fields");
                if (bookNameFields != null && bookNameFields.containsKey("keyword")) {
                    // "bookName.keyword" 存在
                    System.out.println("bookName.keyword exists");
                } else {
                    // "bookName.keyword" 不存在
                    System.out.println("bookName.keyword does not exist");
                }
            }
        }*/
        System.out.println(bookName);
        // 构建查询
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("classification").field("part.keyword").size(30);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termQuery("bookName.keyword", bookName))
                .withAggregations(termsAggregationBuilder)
                .build();

        // 执行查询
        SearchHits<Book> searchHits = elasticsearchRestTemplate.search(searchQuery, Book.class, IndexCoordinates.of("book"));
        // 输出总命中数，用于调试
        long totalHits = searchHits.getTotalHits();
        System.out.println("Total hits: " + totalHits);

        // 处理聚合结果
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
        Terms classification = aggregations.aggregations().get("classification");
        for (Terms.Bucket bucket : classification.getBuckets()) {
            System.out.println("Part: " + bucket.getKeyAsString() + ", Count: " + bucket.getDocCount());
        }

        // 将搜索命中的文档转换为目标类的实例列表
        return classification.getBuckets().stream()
                .map(bucket -> new AbstractMap.SimpleEntry<>(bucket.getKeyAsString(), bucket.getDocCount()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map.Entry<String, Long>> findByBookNamePart(String bookName,String part) {
        System.out.println(bookName+'-'+part);

        // 构建查询
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("classification").field("category.keyword").size(50);
        // 使用boolQuery组合多个查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("bookName.keyword", bookName))
                .must(QueryBuilders.termQuery("part.keyword", part));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withAggregations(termsAggregationBuilder)
                .build();
        // 执行查询
        SearchHits<Book> searchHits = elasticsearchRestTemplate.search(searchQuery, Book.class, IndexCoordinates.of("book"));
        // 输出总命中数，用于调试
        long totalHits = searchHits.getTotalHits();
        System.out.println("Total hits: " + totalHits);
        // 处理聚合结果
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
        Terms classification = aggregations.aggregations().get("classification");
        for (Terms.Bucket bucket : classification.getBuckets()) {
            System.out.println("Category: " + bucket.getKeyAsString() + ", Count: " + bucket.getDocCount());
        }
        // 将搜索命中的文档转换为目标类的实例列表
        return classification.getBuckets().stream()
                .map(bucket -> new AbstractMap.SimpleEntry<>(bucket.getKeyAsString(), bucket.getDocCount()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Figure> getTop50FiguresByCount(){
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSorts(SortBuilders.fieldSort("count").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 50)) // 仅查询前50条记录
                .build();
        // 执行查询，并从Page对象中获取内容
        SearchHits<Figure> searchHits = elasticsearchRestTemplate.search(searchQuery, Figure.class);

        // 将结果转换为List并返回
        // 处理SearchHits以获取Figure对象列表
        List<Figure> figures = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return figures;
    }

    @Override
    public void deleteAll(){
        userRepository.deleteAll();
    }

    @Override
    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    @Override
    public List<Book> getAll(){
        Iterable<Book> books = userRepository.findAll();
        if (books==null){
            return Collections.emptyList();
        }
        List<Book> booklist = new ArrayList<>();
        books.forEach(b -> booklist.add(b));
        return booklist;
    }
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /*
     *      多域查询的交并集理解:
     *                      OR:  只要有一个域中包含入参value被分词后的"一个值"时就返回
     *                      AND: 只要有一个域中包含入参value被分词后的"所有值"时返回
     *
     * @param fields    : Map<String,Float>类型:key为域名,Float为boost值
     *                  boost: 参数被用来提升一个语句的相对权重（ boost 值大于 1 ）或降低相对权重（ boost 值处于 0 到 1 之间），但是这种提升或降低并不是线性的，换句话说，如果一个 boost 值为 2 ，并不能获得两倍的评分 _score 。
     * @param queryString     : 要查询的值 (会对查询条件进行分词)
     * @param analyzer  : 选择分词器[ik_smart粗粒度,ik_max_word细粒度] 默认:ik_max_word细粒度
     * @param operator  : Operator.OR(并集) [默认] 只要分的词有一个和索引字段上对应上则就返回
     *                  Operator.AND(交集)   分的词全部满足的数据返回
     * @param classType :  返回的list里的对象并且通过对象里面@Document注解indexName属性获取查询哪个索引
     * @return java.util.List<T>
     * @explain :  queryString 多条件查询
     * •会对查询条件进行分词。
     * •然后将分词后的查询条件和词条进行等值匹配
     * •默认取并集（OR）
     * •可以指定多个查询字段
     * •query_string：识别query中的连接符（or 、and）*/
    @Override
    public <T> List<T> queryStringQuery(Map<String,Float> fields, String queryString, String analyzer, Operator operator,Class<T> classType){
        //查询条件(词条查询：对应ES query里的queryStringQuery)
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(queryString).fields(fields).analyzer(analyzer).defaultOperator(operator);
        //创建查询条件构建器SearchSourceBuilder(对应ES外面的大括号)
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQueryBuilder);

        /*NativeSearchQuery query = new NativeSearchQueryBuilder().withPageable(PageRequest.of(pageNum==null||pageNum==0?0:pageNum-1,pageSize)).withQuery(queryStringQueryBuilder).build();
        public <T> List<T> selectFindPage(Integer pageNum,Integer pageSize,String key,String value,Class<T> classType){
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            //分词查询(这里指定分词只是分传过来的参数)[它是要和es索引里的分词后数据一一对应才能返回]
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key,value).analyzer("ik_smart");
        *//*例子：前台传了 (山东省:粗粒度分为山东省 细粒度分为：山东省,山东,省)
        es索引库里(山东省济南市 粗粒度分为 山东省,济南市  细粒度分为:山东省,山东,省,济南市,济南,市)
        只有当前台分的词和后台分的词能有一个匹配上就可以*//*
            nativeSearchQueryBuilder.withQuery(matchQueryBuilder);
            nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum==null||pageNum==0?0:pageNum-1,pageSize));
            //4.构建查询对象
            NativeSearchQuery query = nativeSearchQueryBuilder.build();

            SearchHits<T> searchHits = elasticsearchRestTemplate.search(query,classType);
            //获取总记录数
            long totalHits = searchHits.getTotalHits();
            SearchPage<T> page = SearchHitSupport.searchPageFor(searchHits,query.getPageable());
            //组装分页对象
            List<T> pageList = new ArrayList<>();
            Page<T> pageInfo = new PageImpl<>(pageList,query.getPageable(),searchHits.getTotalHits());
            return pageInfo.getContent();
        }*/

        //查询,获取查询结果
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery,classType);
        //获取总记录数
        long totalHits = searchHits.getTotalHits();
        //获取值返回
        return searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    /*boolQuery 查询文档 (对多个查询条件连接。连接方式：must)
        must（and）：条件必须成立
        must和filter配合使用时，max_score（得分）是显示的
        must 可以是单个条件，也可以对各条件 (默认数组形式)
        maxSore(得分) : 即条件匹配度,匹配度越高，得分越高
        * @param fields    :Map<String,String[]> key:es索引库里的域(字段名), value数组:一域多值, 查询的值
        * @param classType : 返回的list里的对象并且通过对象里面@Document注解indexName属性获取查询哪个索引*/
    @Override
    public <T> Result boolQueryBuildByMust(Map<String,String> fields,Integer pageNum,Integer pageSize,String preTags,String postTags, Class<T> classType){
        //构建boolQuery(词条查询：对应ES query里的bool)对多个查询条件连接
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        fields.forEach((key,value) ->{
//            // 对每个字段的每个值创建一个matchQuery，并通过should连接
//            BoolQueryBuilder innerBoolQuery = QueryBuilders.boolQuery();
//            for (String value : values) {
//                MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key, value);
//                innerBoolQuery.should(matchQueryBuilder);
//            }
//            // 将内部的boolQuery作为一个must条件添加到外层的boolQuery中
//            boolQueryBuilder.must(innerBoolQuery);
            //查询条件(词条查询：对应ES query里的termQuery)
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key,value);
            //must（and）：条件必须成立
            boolQueryBuilder.must(matchQueryBuilder);
        });
        // 配置高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        fields.keySet().forEach(field->{
            highlightBuilder.field(new HighlightBuilder.Field(field).preTags(preTags).postTags(postTags));
        });
        //创建查询条件构建器SearchSourceBuilder(对应ES外面的大括号)
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .withPageable(PageRequest.of(pageNum,pageSize)).build();
//                new NativeSearchQuery(boolQueryBuilder);
        //查询,获取查询结果
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery,classType);
        //获取总记录数
        long totalHits = searchHits.getTotalHits();
        //获取值返回
        // 处理高亮字段
        List<T> list = searchHits.getSearchHits().stream().map(hit ->{
            // 在这里处理每个文档的高亮字段
            Map<String,List<String>> highlightFields = hit.getHighlightFields();
            T content = hit.getContent();
            // 根据实际情况将高亮内容设置到content对象的相应字段中
            for (Map.Entry<String,List<String>> stringListEntry : highlightFields.entrySet()){
                String key = stringListEntry.getKey();
                //获取实体反射类
                Class<?> aClass = content.getClass();
                try{
                    //获取该实体属性
                    Field declaredField = aClass.getDeclaredField(key);
                    //权限为私的 解除！
                    declaredField.setAccessible(true);
                    //替换，把高亮字段替换到这个实体对应的属性值上
                    declaredField.set(content, stringListEntry.getValue().get(0));
                }catch (NoSuchFieldException | IllegalAccessException e){
                    e.printStackTrace();
                }
            }
            return content;
        }).collect(Collectors.toList());

//        Page<T> pageInfo = new PageImpl<>(list,nativeSearchQuery.getPageable(),searchHits.getTotalHits());
//        System.out.println(pageInfo.getTotalElements());
//        System.out.println(pageInfo.getTotalPages());
//        System.out.println(pageInfo.getNumber());
//        System.out.println(pageInfo.getSize());
        Result result = new Result();
        result.setTotal(totalHits);
        result.setData(list);
        return result;
//        return searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

    }

    /*boolQuery子句用termsQuery做的测试
     * @param fields    :Map<String,String[]> key:es索引库里的域(字段名), value数组:一域多值, 查询的值(must_not:只要是所有字段都对应不到数组中的值就可以返回)
     * fields 只要数据不包含在 value数组中指定的返
     * @param classType : 返回的list里的对象并且通过对象里面@Document注解indexName属性获取查询哪个索引
     * @return java.util.List<T>
     * @explain : boolQuery：返回的文档必须不满足定义的条件
     *对多个查询条件连接。连接方式：must_not（not）：条件必须不成立*/
    @Override
    public <T> List<T> boolQueryBuildByMustNot(Map<String,String[]> fields, Class<T> classType){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        fields.forEach((key,value) ->{
            TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(key,value);
            boolQueryBuilder.mustNot(termsQueryBuilder);
        });

        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder);
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery,classType);
        long totalHits = searchHits.getTotalHits();
        return searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

     /*
     * @param fields:Map<String,String[]> key:es索引库里的域(字段名), value数组:一域多值, 查询的值(should:只要有一条数据对应的字段包含 value数组的值就返回)
     * 只要有一条数据对应的字段包含 value数组的值就返回
     * @param minimumShouldMatch : 参数定义了至少满足几个子句
     * @param classType: 返回的list里的对象并且通过对象里面@Document注解indexName属性获取查询哪个索引
     * @return java.util.List<T>
     * @explain : boolQuery：返回的文档可能满足should子句的条件.
     * 在一个bool查询中,如果没有must或者filter,有一个或者多个should子句,那么只要满足一个就可以返回
     */
    @Override
    public <T> Result boolQueryBuildByShould(Map<String,String> fields,Integer pageNum,Integer pageSize,String preTags,String postTags, Class<T> classType){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        fields.forEach((key,value) ->{
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key,value);
            boolQueryBuilder.should(matchQueryBuilder);
        });
        boolQueryBuilder.minimumShouldMatch(1);//minimumShouldMatch
        // 配置高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        fields.keySet().forEach(field->{
            highlightBuilder.field(new HighlightBuilder.Field(field).preTags(preTags).postTags(postTags));
        });
        //创建查询条件构建器SearchSourceBuilder(对应ES外面的大括号)
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .withPageable(PageRequest.of(pageNum,pageSize)).build();

        //NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder);
        //查询,获取查询结果
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery,classType);
        //获取总记录数
        long totalHits = searchHits.getTotalHits();

        //获取值返回
        // 处理高亮字段
        List<T> list = searchHits.getSearchHits().stream().map(hit ->{
            // 在这里处理每个文档的高亮字段
            Map<String,List<String>> highlightFields = hit.getHighlightFields();
            T content = hit.getContent();
            // 根据实际情况将高亮内容设置到content对象的相应字段中
            for (Map.Entry<String,List<String>> stringListEntry : highlightFields.entrySet()){
                String key = stringListEntry.getKey();
                //获取实体反射类
                Class<?> aClass = content.getClass();
                try{
                    //获取该实体属性
                    Field declaredField = aClass.getDeclaredField(key);
                    //权限为私的 解除！
                    declaredField.setAccessible(true);
                    //替换，把高亮字段替换到这个实体对应的属性值上
                    declaredField.set(content, stringListEntry.getValue().get(0));
                }catch (NoSuchFieldException | IllegalAccessException e){
                    e.printStackTrace();
                }
            }
            return content;
        }).collect(Collectors.toList());

        Result result = new Result();
        result.setTotal(totalHits);
        result.setData(list);
        return result;
    }

    /* @param field     : 高亮字段 也是 match要查询的字段
     * @param preTags   : 高亮前缀
     * @param postTags  : 高亮后缀
     * @param text      : 查询的值(会分词)
     * @param classType :  返回的list里的对象并且通过对象里面@Document注解indexName属性获取查询哪个索引
     * @return java.util.List<T>
     * @explain :  给查询到的值进行高亮*/
    @Override
    public <T> List<T> highlightBuilder(String field,String preTags,String postTags,String text,Class<T> classType){

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(field,text);
        //设置高亮三要素     field: 你的高亮字段   preTags ：前缀   postTags：后缀
        HighlightBuilder highlightBuilder = new HighlightBuilder().field(field).preTags(preTags).postTags(postTags);
        //创建查询条件构建器SearchSourceBuilder(对应ES外面的大括号)
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(matchQueryBuilder).withHighlightBuilder(highlightBuilder).build();
        //查询,获取查询结果
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery,classType);
        long totalHits = searchHits.getTotalHits();
        return searchHits.getSearchHits().stream().map(searchHit -> {
            //获得结果实体
            T content = searchHit.getContent();
            //所有高亮结果
            Map<String,List<String>> highlightFields = searchHit.getHighlightFields();
            //遍历高亮结果
            for (Map.Entry<String,List<String>> stringListEntry : highlightFields.entrySet()){
                String key = stringListEntry.getKey();
                //获取实体反射类
                Class<?> aClass = content.getClass();
                try{
                    //获取该实体属性
                    Field declaredField = aClass.getDeclaredField(key);
                    //权限为私的 解除！
                    declaredField.setAccessible(true);
                    //替换，把高亮字段替换到这个实体对应的属性值上
                    declaredField.set(content, stringListEntry.getValue().get(0));
                }catch (NoSuchFieldException | IllegalAccessException e){
                    e.printStackTrace();
                }
            }
            return content;
        }).collect(Collectors.toList());
    }


    /*
     * https://www.cnblogs.com/Alexephor/p/11408446.html(Elasticsearch之建议器suggester)写的很详细
     * 词条建议器（term suggester）对用户搜索的内容做纠正帮助用户搜索到精确度高的关键字
     * @param fieldName : 从fieldName字段中获取候选建议的字段。这是一个必需的选项，需要全局设置或根据建议设置。Keyword字段
     * @param text      : 建议文本，建议文本是必需的选项，可以通过全局（多个建议器中查询相同的内容）或者按照单个建议器的格式来。
     * @param classType : 返回的list里的对象并且通过对象里面@Document注解indexName属性获取查询哪个索引
     * @return java.util.List<java.lang.String>
     * @explain :　词条建议器（term suggester）主要做纠正 但是是短语就不能做了(Keyword字段)*/
    @Override
    public List<String> termSuggestion(String fieldName,String text,Class<?> classType){
        //定义反参容器
        List<String> stringList = new ArrayList<>();
        //构建纠正词条对象  词条建议器(只要是词,短的 比如姓名)
        TermSuggestionBuilder termSuggestionBuilder = SuggestBuilders.termSuggestion(fieldName).text(text);
        /*termSuggestionBuilder.suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS);
        建议模式（控制提供建议词的方式）：
        1. missing：默认方式，仅在‘要搜索词项’不在索引中存在时，才提供建议词；
        2. popular：仅提供频率比‘要搜索词项’高的建议词；
        3. always：总是提供建议词；*/
        termSuggestionBuilder.sort(SortBy.SCORE);
        /*建议词的排序方式：
        1. score：先按评分排序，再按文档频率、term顺序排；
        2. frequency：先按文档频率排序，再按评分、term顺序排*/
        //创建搜索提示对象 进行封装词条纠正
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        //  termSuggestionBuilder:随便起的搜索补全的名字(后面会用到)
        suggestBuilder.addSuggestion("termSuggestionBuilder",termSuggestionBuilder);
        // 查询,获取查询结果
        SearchResponse searchResponse = elasticsearchRestTemplate.suggest(suggestBuilder, IndexCoordinates.of(classType.getAnnotation(Document.class).indexName()));
        //获取反参中的词条纠正结果
        Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggestionBuilder = searchResponse.getSuggest().getSuggestion("termSuggestionBuilder");
        // 处理返回
        List<String> suggests = suggestionBuilder.getEntries().stream().map(x -> x.getOptions().stream().map(y -> y.getText().toString()).collect(Collectors.toList())).findFirst().get();
        // 将词条纠正内容保存到容器返回
        for (String suggest:suggests){
            stringList.add(suggest);
            System.err.println("suggest = "+suggest);
        }
        return stringList;
    }

    /*
     * @param key       :  es里索引的域(字段名)
     * @param value     : 查询的值
     * @param classType : 返回的list里的对象并且通过对象里面@Document注解indexName属性获取查询哪个索引
     * @return java.util.List<T>
     * @explain :   wildcardQuery模糊查询(会对查询条件分词，还可以使用通配符)[?:表示任意单个字符][*:表示0或多个字符]*/
    @Override
    public <T> List<T> wildcardQuery(String key,String value,Class<T> classType){
        //查询条件(词条查询：对应ES query里的wildcard)
        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(key,value);
        //创建查询条件构建器SearchSourceBuilder(对应ES外面的大括号)
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(wildcardQueryBuilder);
        //查询,获取查询结果
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery,classType);
        //获取总记录数
        long totalHits = searchHits.getTotalHits();
        //获取值返回
        return searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    /*
     * @param pageNum   : 分页查询的页数
     * @param pageSize  : 分页查询返回的每页个数
     * @param key       : 查询es的字段名
     * @param value     : 要查询字段名中的值
     * @param classType : 返回的类类型*/
    @Override
    public <T> List<T> selectFindPage(String value,Integer pageNum,Integer pageSize,Class<T> classType){
        //构建搜索条件
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("宋苏轼撰","author","summary"))
                .withPageable(PageRequest.of(pageNum,pageSize))
                .build();
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(query,classType);
        //获取总记录数
        long totalHits = searchHits.getTotalHits();
        SearchPage<T> page = SearchHitSupport.searchPageFor(searchHits,query.getPageable());
        //组装分页对象
        List<T> pageList = new ArrayList<>();
        Page<T> pageInfo = new PageImpl<>(pageList,query.getPageable(),searchHits.getTotalHits());
        return pageInfo.getContent();
    }

    @Override
    public <T> List<T> multiMatchQuery(String keyword,String[] fields,Class<T> classType){
        System.out.println(Arrays.toString(fields));
        String[] fields1 = {"id","bookName","bookAuthor","title","location","part","category","edition","author","summary"};  //"id",
        System.out.println(Arrays.toString(fields1));
        System.out.println(fields1);
        System.out.println(fields);
        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword,fields1);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        //查询,获取查询结果
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(query,classType);
        //获取总记录数
        long totalHits = searchHits.getTotalHits();
        //获取值返回
        return searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public <T> Result multiMatchQueryPage(String keyword,Integer pageNum,Integer pageSize,String preTags,String postTags,Class<T> classType){
        //设置查询的字段
        String[] fields = {"bookName","bookAuthor","title","location","part","category","edition","author","summary"};
        //查询条件
        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword,fields);
        //设置高亮三要素     fields: 你的高亮字段   preTags ：前缀   postTags：后缀
        HighlightBuilder.Field[] fields1 = new HighlightBuilder.Field[fields.length];
        for(int i=0;i<fields.length;i++){
            fields1[i] = new HighlightBuilder.Field(fields[i]).preTags(preTags).postTags(postTags);
        }
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(queryBuilder)
                .withHighlightFields(fields1)
                .withPageable(PageRequest.of(pageNum,pageSize))
                .build();
        //查询,获取查询结果
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(query,classType);
        //获取总记录数
        long totalHits = searchHits.getTotalHits();
        List<T> list = new ArrayList<>();
        for (SearchHit<T> searchHit :searchHits.getSearchHits()){
            T content = searchHit.getContent();
            //获取高亮信息，把高亮字段复制到对象上
            Map<String,List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中
            for (Map.Entry<String, List<String>> stringListEntry : highlightFields.entrySet()) {
                String key = stringListEntry.getKey();
                //获取实体反射类
                Class<?> aClass = content.getClass();
                try {
                    //获取该实体属性
                    Field declaredField = aClass.getDeclaredField(key);
                    //权限为私的 解除！
                    declaredField.setAccessible(true);
                    //替换，把高亮字段替换到这个实体对应的属性值上
                    declaredField.set(content, stringListEntry.getValue().get(0));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            list.add(content);
        }
        //SearchPage<T> page = SearchHitSupport.searchPageFor(searchHits,query.getPageable());
        //组装分页对象

        /*List<T> pageList = new ArrayList<>();
        for (SearchHit<T> each : page) {
            T vo = each.getContent();
            pageList.add(vo);
        }*/
        Page<T> pageInfo = new PageImpl<>(list,query.getPageable(),searchHits.getTotalHits());
//        System.out.println(pageInfo.getTotalElements());
//        System.out.println(pageInfo.getTotalPages());
//        System.out.println(pageInfo.getNumber());
//        System.out.println(pageInfo.getSize());
        Result result = new Result();
        result.setTotal(totalHits);
        result.setData(pageInfo.getContent());
        return result;
    }
}

