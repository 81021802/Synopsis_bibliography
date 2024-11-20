import myaxios from "@/utils/myaxios";
import Qs from "qs";

export default{
    getList(){
        const promise1 = myaxios({
            method:'get',
            url:'/book/all'
        })
        return promise1
    },
    // search(input){
    //     const promise2 = myaxios({
    //         method:'post',
    //         url:'/book/queryStringQuery',
    //         data:{"fields":[{"field":"bookName","score":"1.0"},{"field":"part","score":"1.0"},{"field":"category","score":"1.0"},{"field":"author","score":"1.0"},{"field":"summary","score":"1.0"}],
    //         "queryString":input,
    //         "analyzer":"ik_max_word",
    //         "operator":"or"}
    //     })
    //     return promise2
    // },
    searchPage(input,page,size){    
        let param = new URLSearchParams()
        param.append("keyword",input)
        param.append("pageNum",page)
        param.append("pageSize",size)
        param.append("preTags","<span style=\"color: red\">")
        param.append("postTags","</span>")
        const promise3 = myaxios({
            method:'post',
            url:'/book/multiMatchQueryPage',
            data:param
        })
        return promise3
    },
    onSearch(searchForm){
        const promise4 = myaxios({
            method:'post',
            url:'/book/boolQueryBuildByMust',
            data:searchForm
        })
        return promise4
    },
    onSearchor(searchForm){
        const promise5 = myaxios({
            method:'post',
            url:'/book/boolQueryBuildByShould',
            data:searchForm
        })
        return promise5
    },
    aggregation(book){
        const promise6 = myaxios({
            method:'post',
            url:'/book/findByBookName',
            data:{ bookName: book },
        })
        return promise6
    },
    aggregation1(book,part){
        const promise7 = myaxios({
            method:'post',
            url:'/book/findByBookNamePart',
            data:{ bookName: book, part: part},
        })
        return promise7
    },
    bookAggregation(){
        const promise8 = myaxios({
            method:'post',
            url:'/book/groupByBookName'
        })
        return promise8
    },
    editionAggregation(){
        const promise9 = myaxios({
            method:'post',
            url:'/book/groupByEditionDynasty'
        })
        return promise9
    },
    figureAggregation(){
        const promise10 = myaxios({
            method:'post',
            url:'/book/groupByFigureDynasty'
        })
        return promise10
    },
    CountTop50Figures(){
        const promise11 = myaxios({
            method:'post',
            url:'/book/getTop50FiguresByCount'
        })
        return promise11
    },
    getEntity(id){
        const promise12 = myaxios({
            method:'get',
            url:`/book/entity/${id}`
        })
        return promise12
    },
}